pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Set Permissions') {
            steps {
                sh 'chmod +x gradlew'
            }
        }
        stage('Build') {
            environment {
                JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
                PATH = "${JAVA_HOME}/bin:${PATH}"
            }
            steps {
                sh './gradlew clean assembleRelease'
            }
        }
        stage('Test') {
            environment {
                JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
                PATH = "${JAVA_HOME}/bin:${PATH}"
            }
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/app/build/test-results/testDebugUnitTest/*.xml'
                }
            }
        }
    }
    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
        always {
            archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk', fingerprint: true
            junit '**/app/build/test-results/testDebugUnitTest/*.xml' // Adjust path as necessary
        }
    }
}
