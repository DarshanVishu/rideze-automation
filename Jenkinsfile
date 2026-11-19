pipeline {
    agent any

    tools {
        jdk 'JDK-21'     // Ensure this matches your Global Tool Configuration name in Jenkins
        maven 'Maven-3'  // Ensure this matches your Maven installation name in Jenkins
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['staging', 'qa', 'local'],
            description: 'Target environment URL profile'
        )
        choice(
            name: 'SUITE_XML',
            choices: ['testng-smoke.xml', 'testng-regression.xml', 'testng-api.xml'],
            description: 'Select the TestNG suite to execute'
        )
        string(
            name: 'THREAD_COUNT',
            defaultValue: '3',
            description: 'Number of parallel threads for execution'
        )
    }

    triggers {
        // Runs automatically every night at 2:00 AM
        cron('H 2 * * *')
    }

    environment {
        // Map credentials securely from Jenkins Credentials Manager
        TEST_USER_EMAIL = credentials('qa-staging-email')
        TEST_USER_PASS  = credentials('qa-staging-password')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile & Validate') {
            steps {
                bat 'mvn clean test-compile'
            }
        }

        stage('Execute Automated Suite') {
            steps {
                script {
                    def baseUrl = ""
                    if (params.ENVIRONMENT == 'staging') {
                        baseUrl = "https://staging.rideze.in" // Replace with your actual staging URL
                    } else if (params.ENVIRONMENT == 'qa') {
                        baseUrl = "https://qa.rideze.in"
                    } else {
                        baseUrl = "http://localhost:3000"
                    }

                    bat "mvn test -DsuiteXmlFile=src/test/resources/${params.SUITE_XML} -DbaseUrl=${baseUrl} -DthreadCount=${params.THREAD_COUNT} -Dheadless=true"
                        mvn test \
                        -DsuiteXmlFile=src/test/resources/${params.SUITE_XML} \
                        -DbaseUrl=${baseUrl} \
                        -DthreadCount=${params.THREAD_COUNT} \
                        -Dheadless=true
                    """
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
        failure {
            echo "Build failed for ${env.JOB_NAME} #${env.BUILD_NUMBER} on ${params.ENVIRONMENT}."
            // Add Slack or Mailer step here once configured
        }
    }
}
