cat << 'EOF' > Jenkinsfile
pipeline {
    agent any

    parameters {
        string(
            name: 'SUITE_XML',
            defaultValue: 'src/test/resources/testng.xml',
            description: 'Path to TestNG XML file (e.g. src/test/resources/testng.xml)'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browsers in headless mode'
        )
        string(
            name: 'BASE_URL',
            defaultValue: '',
            description: 'Optional target URL override (leave blank to use config.properties)'
        )
    }

    environment {
        ALLURE_RESULTS_DIR = 'target/allure-results'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Environment') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            java -version
                            mvn -version
                        '''
                    } else {
                        bat '''
                            java -version
                            call mvn -version
                        '''
                    }
                }
            }
        }

        stage('Execute Rideze Automation Suite') {
            steps {
                script {
                    def suiteArg = fileExists(params.SUITE_XML.trim()) ? "-DsuiteXmlFile=${params.SUITE_XML.trim()}" : ""
                    def urlArg = params.BASE_URL?.trim() ? "-DbaseUrl=${params.BASE_URL.trim()} -Durl=${params.BASE_URL.trim()}" : ""

                    def mvnCmd = "mvn clean test " +
                                 "${suiteArg} " +
                                 "-Dheadless=${params.HEADLESS} " +
                                 "${urlArg} " +
                                 "-Dallure.results.directory=${ALLURE_RESULTS_DIR}"

                    if (isUnix()) {
                        sh "${mvnCmd}"
                    } else {
                        bat "call ${mvnCmd}"
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) {
                    sh '''
                        if [ -d "allure-results" ] && [ "$(ls -A allure-results 2>/dev/null)" ]; then
                            mkdir -p target/allure-results
                            cp -r allure-results/* target/allure-results/
                        fi
                        if [ -d "target/allure-results" ]; then
                            mvn io.qameta.allure:allure-maven:2.12.0:report
                        fi
                    '''
                } else {
                    bat '''
                        if exist allure-results (
                            if not exist target\\allure-results mkdir target\\allure-results
                            xcopy /E /I /Y allure-results target\\allure-results >nul
                        )
                        if exist target\\allure-results (
                            call mvn io.qameta.allure:allure-maven:2.12.0:report
                        )
                    '''
                }
            }

            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'

            archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**, target/surefire-reports/**, screenshots/**',
                             allowEmptyArchive: true
        }
    }
}
EOF
