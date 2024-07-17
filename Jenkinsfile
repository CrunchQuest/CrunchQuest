pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out the code...'
                checkout scm
            }
        }
        stage('Set Permissions') {
            steps {
                echo 'Setting permissions for gradlew...'
                sh 'chmod +x gradlew'
            }
        }
        stage('Build') {
            environment {
                JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
                PATH = "${JAVA_HOME}/bin:${env.PATH}"
            }
            steps {
                echo 'Building the project...'
                sh './gradlew clean assembleRelease'
            }
        }
        stage('Test') {
            environment {
                JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
                PATH = "${JAVA_HOME}/bin:${env.PATH}"
            }
            steps {
                echo 'Running tests...'
                sh './gradlew test'
            }
            post {
                always {
                    echo 'Collecting test results...'
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
            echo 'Archiving artifacts and test results...'
            archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk', fingerprint: true
            junit '**/app/build/test-results/testDebugUnitTest/*.xml' // Adjust path as necessary
        }
    }
}
