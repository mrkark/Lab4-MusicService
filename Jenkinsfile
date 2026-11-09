pipeline {
    agent any

    environment {
        JAVA_HOME = 'D:\\Program Files\\Java\\jdk-23'
        PATH = "D:\\Program Files\\Java\\jdk-23\\bin;C:\\Program Files\\JetBrains\\IntelliJIdea2026.1\\plugins\\maven\\lib\\maven3\\bin;${env.PATH}"
    }

    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }
        // Этап статического анализа кода Checkstyle (ЛР №5)
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
        // Индивидуальное задание (Вариант 11, ЛР №3/4):
        // Проверка и разрешение зависимостей перед сборкой пакета
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
