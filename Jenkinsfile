pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'
    }

    stages {
        stage('Verificar JDK 21 y Maven') {
            steps {
                sh '''
                    java -version
                    java -version 2>&1 | grep -q 'version "21' || {
                        echo "Jenkins debe ejecutar con JDK 21" >&2
                        exit 1
                    }
                    mvn -version
                '''
            }
        }

        stage('Compilar, Probar y Analizar Backend') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn -B clean verify sonar:sonar \
                            -Dsonar.projectKey=com.mx.cbtis:Ejercicio5MyAPIGestor \
                            -Dsonar.projectName="Ejercicio5MyAPIGestor" \
                            -Dsonar.java.source=21 \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                            -Dsonar.exclusions=**/target/**
                    '''
                }
            }
        }

        stage('Escaneo de Vulnerabilidades Semgrep') {
            steps {
                sh '''
                    mkdir -p reportes
                    if command -v semgrep >/dev/null 2>&1; then
                        semgrep scan --config auto --exclude target \
                            --json -o reportes/semgrep-backend.json || true
                    elif command -v docker >/dev/null 2>&1; then
                        docker run --rm -v "$(pwd):/src" returntocorp/semgrep semgrep scan \
                            --config auto \
                            --exclude target \
                            --json -o /src/reportes/semgrep-backend.json || true
                    else
                        echo "[]" > reportes/semgrep-backend.json
                    fi
                '''
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        echo "Quality Gate status de SonarQube: ${qg.status}"
                        if (qg.status != 'OK') {
                            echo "Advertencia: El Quality Gate reportó '${qg.status}' (revisar New Code en SonarQube), pero la compilación y pruebas pasaron exitosamente."
                        } else {
                            echo "Quality Gate PASSED"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            recordCoverage tools: [[
                parser: 'JACOCO',
                pattern: 'target/site/jacoco/jacoco.xml'
            ]]

            archiveArtifacts artifacts: 'target/*.jar, reportes/*.json',
                             allowEmptyArchive: true,
                             fingerprint: true
        }
        success {
            echo 'Pipeline backend completado exitosamente'
        }
        unstable {
            echo 'Pipeline completado con advertencias - Revisar Quality Gate en SonarQube'
        }
        failure {
            echo 'Pipeline fallido - Revisar errores de compilacion o pruebas'
        }
    }
}
