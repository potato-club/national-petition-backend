pipeline {
    agent any

    stages {

        stage('CheckOut') {
            steps {
                echo 'checkout backend...'
                checkout([$class: 'GitSCM', branches: [[name: '*/develop']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/potato-club/national-petition-backend.git']]])
            }
        }

        stage('Build And Test') {
            steps {
                echo 'build backend....'

                dir('./') {
                    sh '''
                        ./gradlew clean build
                    '''
                }
            }
        }

    }
}