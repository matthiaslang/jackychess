pipeline {
    agent {
        dockerfile {
            args '-v /root/.m2:/root/.m2'
        }
    }
    triggers { pollSCM('*/30 * * * *') }

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
                sh 'mvn test '
            }

            post {
                always {
                    junit testResults: 'target/surefire-reports/**/*.xml, target/failsafe-reports/**/*.xml'
                }
            }
        }

        stage('IT Test') {
            steps {
                catchError/*(buildResult: 'SUCCESS', stageResult: 'FAILURE')*/ {
                    sh 'mvn verify '
                }
            }

            post {
                always {
                    junit testResults: 'target/surefire-reports/**/*.xml, target/failsafe-reports/**/*.xml'
                }
            }
        }
    }
}