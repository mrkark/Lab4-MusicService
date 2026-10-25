pipeline {
    agent any

    environment {
        JAVA_HOME = 'D:\\Program Files\\Java\\jdk-23'
        PATH = "D:\\Program Files\\Java\\jdk-23\\bin;C:\\Program Files\\JetBrains\\IntelliJIdea2026.1\\plugins\\maven\\lib\\maven3\\bin;${env.PATH}"
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        // Индивидуальное задание (Вариант 11):
        // Отдельный этап с отдельной Maven-командой перед Package.
        // Загружает и проверяет доступность всех зависимостей проекта перед упаковкой в JAR.
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
            echo 'Pipeline успешно завершен! Проект успешно получен из Git, протестирован и упакован в JAR.'
        }
        failure {
            echo 'Сборка Pipeline завершилась с ошибкой!'
        }
    }
}
