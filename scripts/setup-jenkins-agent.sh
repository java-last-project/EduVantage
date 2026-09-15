#!/bin/bash

set -e

JENKINS_URL="http://168.138.42.29:8080"
AGENT_NAME="ec2-build"
AGENT_DIR="/home/ubuntu/jenkins-agent"
AGENT_ENV="/home/ubuntu/jenkins-agent.env"

echo "========================================"
echo " Jenkins Agent Setup"
echo "========================================"

if [ ! -f "$AGENT_ENV" ]; then
	echo "Jenkins Agent 설정 파일이 없습니다."
	echo "경로: $AGENT_ENV"
	echo
	echo "다음 형식으로 생성하세요."
	echo "JENKINS_AGENT_SECRET=..."
	exit 1
fi

chmod 600 "$AGENT_ENV"

. "$AGENT_ENV"

if [ -z "$JENKINS_AGENT_SECRET" ]; then
	echo "JENKINS_AGENT_SECRET이 설정되지 않았습니다."
	exit 1
fi

mkdir -p "$AGENT_DIR"

echo "Jenkins agent.jar 다운로드"

curl -fsSL \
	"$JENKINS_URL/jnlpJars/agent.jar" \
	-o "$AGENT_DIR/agent.jar"

chmod 644 "$AGENT_DIR/agent.jar"

echo "systemd 서비스 생성"

sudo tee /etc/systemd/system/jenkins-agent.service > /dev/null <<EOF
[Unit]
Description=Jenkins EC2 Build Agent
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=$AGENT_DIR
EnvironmentFile=$AGENT_ENV
ExecStart=/usr/bin/java -jar $AGENT_DIR/agent.jar -url $JENKINS_URL -secret \${JENKINS_AGENT_SECRET} -name $AGENT_NAME -webSocket -workDir $AGENT_DIR
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable jenkins-agent
sudo systemctl restart jenkins-agent

sleep 3

echo
echo "========================================"
echo " Jenkins Agent 상태"
echo "========================================"

sudo systemctl --no-pager --full status jenkins-agent