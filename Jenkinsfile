pipeline {
    agent any

    environment {
        JAVA_HOME = 'D:\\Program Files\\Java\\jdk-23'
        PATH = "D:\\Program Files\\Java\\jdk-23\\bin;C:\\Program Files\\JetBrains\\IntelliJIdea2026.1\\plugins\\maven\\lib\\maven3\\bin;${env.PATH}"
    }

    triggers {
        pollSCM('*/2 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }
        stage('Static Analysis') {
            steps {
                bat 'mvn checkstyle:check'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Resolve Dependencies') {
            steps {
                bat 'mvn dependency:resolve'
            }
        }
        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }
    }
    post {
        success {
            echo 'Pipeline успешно завершен! Проект прошел компиляцию, Checkstyle-анализ, тестирование и упаковку в JAR.'
        }
        failure {
            echo 'Сборка Pipeline завершилась с ошибкой!'
        }
    }
}
