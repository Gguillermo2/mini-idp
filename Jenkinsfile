pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'Java21'
    }

    environment {
        GEMINI_API_KEY = credentials('gemini-api-key')
    }

    stages {

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado correctamente.'
        }

        failure {
            echo 'El pipeline falló.'
        }
    }
}

