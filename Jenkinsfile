pipeline {
    agent {
        dockerfile {
            args '-v /root/.m2:/root/.m2'
        }
    }
    triggers { pollSCM('*/15 * * * *') }

    stages {


        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests '
            }

            post {
                always {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn verify '
            }

            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }
}