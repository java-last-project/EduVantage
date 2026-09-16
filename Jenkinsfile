pipeline {
	// 빌드는 EC2에서
	agent {
		label 'ec2-build'
	}

	environment {
		DOCKER_IMAGE = 'kisd09/eduvantage'
		IMAGE_TAG = "${BUILD_NUMBER}"

		DEPLOYMENT_NAME = 'eduvantage'
		CONTAINER_NAME = 'eduvantage'
		NAMESPACE = 'default'
	}

	stages {
		stage('Checkout') {
			steps {
				checkout scm
			}
		}

		stage('Build & Test') {
        	steps {
        	// Jenkins 환경변수 파일 사용
        		withCredentials([
        			file(
        				credentialsId: 'eduvantage-env',
        				variable: 'ENV_FILE'
        			)
        		]) {
        			sh '''
        				chmod +x gradlew

        				set -a
        				. "$ENV_FILE"
        				set +a

        				./gradlew clean build
        			'''
        		}
        	}
        }

		stage('Docker Build') {
			steps {
				// 빌드번호 태그는 배포용, latest는 그냥 최신 이미지용
				sh '''
					docker build \
						-t ${DOCKER_IMAGE}:${IMAGE_TAG} \
						-t ${DOCKER_IMAGE}:latest \
						.
				'''
			}
		}

		stage('Docker Push') {
			steps {
				// Jenkins Credentials ID: dockerhub
				withCredentials([
					usernamePassword(
						credentialsId: 'dockerhub',
						usernameVariable: 'DOCKER_USERNAME',
						passwordVariable: 'DOCKER_PASSWORD'
					)
				]) {
					sh '''
						echo "$DOCKER_PASSWORD" | docker login \
							-u "$DOCKER_USERNAME" \
							--password-stdin

						docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
						docker push ${DOCKER_IMAGE}:latest
					'''
				}
			}
		}

		stage('Deploy') {
			steps {
				sh '''
					kubectl apply -f k8s/service.yaml
					kubectl apply -f k8s/deployment.yaml

					# 이번 빌드 이미지로 교체
					kubectl set image \
						deployment/${DEPLOYMENT_NAME} \
						${CONTAINER_NAME}=${DOCKER_IMAGE}:${IMAGE_TAG} \
						-n ${NAMESPACE}
				'''
			}
		}

		stage('Rollout Check') {
			steps {
				script {
					try {
						// 새 버전 정상적으로 뜨는지 확인
						sh '''
							kubectl rollout status \
								deployment/${DEPLOYMENT_NAME} \
								-n ${NAMESPACE} \
								--timeout=180s
						'''
					} catch (Exception e) {
						echo '배포 실패 - 이전 버전으로 롤백'

						sh '''
							kubectl rollout undo \
								deployment/${DEPLOYMENT_NAME} \
								-n ${NAMESPACE}
						'''

						throw e
					}
				}
			}
		}
	}

	post {
		success {
			echo "배포 완료: ${DOCKER_IMAGE}:${IMAGE_TAG}"
		}

		failure {
			echo "파이프라인 실패: BUILD_NUMBER=${BUILD_NUMBER}"
		}

		always {
			// 로그인 정보 남기지 않게 로그아웃
			sh '''
				docker logout || true
			'''
		}
	}
}