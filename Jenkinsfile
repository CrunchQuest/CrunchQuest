pipeline {
    agent any 

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://gitlab.com/crunchquest/CrunchQuest.git', branch: 'main', credentialsId: 'secret-auth'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew assembleRelease' // Adjust as needed for your build type
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test' // Run unit tests
            }
        }
        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: '**/*.apk', allowEmptyArchive: true
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
