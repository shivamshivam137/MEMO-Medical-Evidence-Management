pipeline {
    agent any

    environment {
        ANDROID_HOME = '/opt/android-sdk'
        GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
    }

    stages {
        stage('1. Checkout SCM') {
            steps {
                echo 'Checking out source code from GitHub repository...'
                checkout scm
            }
        }

        stage('2. Prepare Environment') {
            steps {
                echo 'Verifying Java & Gradle environment...'
                sh 'java -version'
                dir('android') {
                    sh 'chmod +x gradlew'
                    sh './gradlew --version'
                }
            }
        }

        stage('3. Compile') {
            steps {
                echo 'Compiling Kotlin sources...'
                dir('android') {
                    sh './gradlew compileDebugKotlin'
                }
            }
        }

        stage('4. Unit Tests') {
            steps {
                echo 'Executing Android Unit Tests...'
                dir('android') {
                    sh './gradlew testDebugUnitTest --continue'
                }
            }
            post {
                always {
                    junit testResults: 'android/app/build/test-results/**/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('5. Static Code Analysis (Lint)') {
            steps {
                echo 'Running Android Lint...'
                dir('android') {
                    sh './gradlew lintDebug'
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'android/app/build/reports/lint-results-debug.html', allowEmptyArchive: true
                }
            }
        }

        stage('6. Assemble Debug APK') {
            steps {
                echo 'Building Android Debug APK artifact...'
                dir('android') {
                    sh './gradlew assembleDebug'
                }
            }
        }

        stage('7. Archive Build Artifacts') {
            steps {
                echo 'Archiving generated APK...'
                archiveArtifacts artifacts: 'android/app/build/outputs/apk/debug/*.apk', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'MEMO Build Pipeline Completed Successfully!'
        }
        failure {
            echo 'Build Failed. Check stage logs for details.'
        }
    }
}
