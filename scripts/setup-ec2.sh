#!/bin/bash

set -e

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
SECRET_FILE="${1:-/home/ubuntu/eduvantage.env}"

echo "========================================"
echo " EduVantage EC2 Setup"
echo "========================================"

# --------------------------------------------------
# 1. 기본 패키지
# --------------------------------------------------

echo "[1/8] 기본 패키지 설치"

sudo apt update
sudo apt install -y \
	openjdk-21-jdk \
	git \
	curl \
	ca-certificates

# --------------------------------------------------
# 2. Swap 4GB
# --------------------------------------------------

echo "[2/8] Swap 설정"

if ! swapon --show | grep -q '/swapfile'; then
	if [ ! -f /swapfile ]; then
		sudo fallocate -l 4G /swapfile
		sudo chmod 600 /swapfile
		sudo mkswap /swapfile
	fi

	sudo swapon /swapfile

	if ! grep -q '^/swapfile ' /etc/fstab; then
		echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
	fi
fi

sudo sysctl vm.swappiness=10

if [ ! -f /etc/sysctl.d/99-eduvantage.conf ]; then
	echo 'vm.swappiness=10' | sudo tee /etc/sysctl.d/99-eduvantage.conf
fi

# --------------------------------------------------
# 3. Docker
# --------------------------------------------------

echo "[3/8] Docker 설치"

if ! command -v docker >/dev/null 2>&1; then
	curl -fsSL https://get.docker.com | sudo sh
fi

sudo systemctl enable docker
sudo systemctl start docker

# Docker가 이미 설치되어 있어도 ubuntu 계정의 그룹 권한은 항상 확인
if ! id -nG "$USER" | grep -qw docker; then
	sudo usermod -aG docker "$USER"
	echo "Docker 그룹에 $USER 계정을 추가했습니다."
	echo "새 SSH 세션부터 sudo 없이 docker 명령을 사용할 수 있습니다."
fi

# --------------------------------------------------
# 4. k3s
# --------------------------------------------------

echo "[4/8] k3s 설치"

if ! command -v k3s >/dev/null 2>&1; then
	curl -sfL https://get.k3s.io | sh -
fi

sudo systemctl enable k3s
sudo systemctl start k3s

echo "k3s 준비 대기"

until sudo k3s kubectl get nodes >/dev/null 2>&1; do
	sleep 2
done

# ubuntu 계정에서 sudo 없이 kubectl을 사용할 수 있도록 kubeconfig 복사
mkdir -p "$HOME/.kube"

sudo cp /etc/rancher/k3s/k3s.yaml "$HOME/.kube/config"
sudo chown "$USER:$USER" "$HOME/.kube/config"
chmod 600 "$HOME/.kube/config"

# 현재 setup 스크립트에서 바로 kubectl 사용
export KUBECONFIG="$HOME/.kube/config"

# 이후 SSH 로그인에서도 동일한 kubeconfig 사용
if ! grep -qxF 'export KUBECONFIG="$HOME/.kube/config"' "$HOME/.bashrc"; then
	echo 'export KUBECONFIG="$HOME/.kube/config"' >> "$HOME/.bashrc"
fi

# --------------------------------------------------
# 5. EC2 Private IP
# --------------------------------------------------

echo "[5/8] EC2 Private IP 확인"

PRIVATE_IP=$(hostname -I | awk '{print $1}')

if [ -z "$PRIVATE_IP" ]; then
	echo "Private IP를 확인할 수 없습니다."
	exit 1
fi

echo "Private IP: $PRIVATE_IP"

# --------------------------------------------------
# 6. Docker Compose 인프라
# --------------------------------------------------

echo "[6/8] Docker Compose 인프라 실행"

cd "$PROJECT_DIR"

# docker-compose.yml의 KAFKA_HOST에 사용
cat > .env <<EOF
KAFKA_HOST=$PRIVATE_IP
EOF

# 현재 실행 중인 셸에는 새 docker 그룹 권한이 아직 반영되지 않을 수 있으므로 sudo 사용
sudo docker compose up -d

# --------------------------------------------------
# 7. Kubernetes 환경 설정
# --------------------------------------------------

echo "[7/8] Kubernetes ConfigMap / Secret 설정"

kubectl create configmap eduvantage-config \
	--from-literal=KAFKA_BOOTSTRAP_SERVERS="${PRIVATE_IP}:29092" \
	--from-literal=REDIS_HOST="${PRIVATE_IP}" \
	--from-literal=REDIS_PORT="7000" \
	--dry-run=client \
	-o yaml | kubectl apply -f -

if [ ! -f "$SECRET_FILE" ]; then
	echo
	echo "Secret 파일을 찾을 수 없습니다."
	echo "경로: $SECRET_FILE"
	echo
	echo "사용법:"
	echo "./scripts/setup-ec2.sh /home/ubuntu/eduvantage.env"
	exit 1
fi

chmod 600 "$SECRET_FILE"

kubectl create secret generic eduvantage-secret \
	--from-env-file="$SECRET_FILE" \
	--dry-run=client \
	-o yaml | kubectl apply -f -

# --------------------------------------------------
# 8. Kubernetes 배포
# --------------------------------------------------

echo "[8/8] EduVantage Kubernetes 배포"

kubectl apply -f "$PROJECT_DIR/k8s/service.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/deployment.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/ingress.yaml"

echo
echo "Deployment 상태 확인"
kubectl rollout status deployment/eduvantage --timeout=180s || true

echo
echo "========================================"
echo " Setup 결과"
echo "========================================"

echo
echo "[Java]"
java -version

echo
echo "[Docker]"
docker --version
docker compose version

echo
echo "[k3s]"
k3s --version

echo
echo "[Swap]"
swapon --show

echo
echo "[Docker Compose]"
sudo docker compose -f "$PROJECT_DIR/docker-compose.yml" ps

echo
echo "[Kubernetes]"
kubectl get nodes
kubectl get pods
kubectl get deployment
kubectl get service
kubectl get ingress

echo
echo "========================================"
echo " EC2 Setup 완료"
echo "========================================"
echo
echo "Docker 그룹 권한은 새 SSH 세션부터 적용됩니다."
echo "현재 SSH 연결을 종료한 뒤 다시 접속하면 sudo 없이 docker를 사용할 수 있습니다."