pipeline {
    agent any 
    environment {
        ANDROID_HOME = '/home/satriagucci/Android/Sdk'
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://gitlab.com/crunchquest/CrunchQuest.git', 
                branch: 'main', 
                credentialsId: 'cq-gitlab-jenkins-token'
            }
        }
        stage('Set Permissions') {
            steps {
                sh 'chmod +x gradlew'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew assembleRelease' // Adjust as needed for your build type
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: '**/app/build/outputs/apk/release/*.apk', fingerprint: true
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
