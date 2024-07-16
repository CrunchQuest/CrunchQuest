pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://gitlab.com/yourusername/yourrepo.git'
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

        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk', fingerprint: true
            }
        }
    }

    post {
        always {
            junit 'build/test-results/**/*.xml' // Adjust path as necessary
            mail to: 'fngevnthppv@gmail.com',
                 subject: "Build ${currentBuild.fullDisplayName} - ${currentBuild.currentResult}",
                 body: "Check console output at ${env.BUILD_URL} to view the results."
        }
    }
}
