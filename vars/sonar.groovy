def call(String repourl){

    podTemplate(containers: [
                        containerTemplate(name: 'sonar-cli', image: 'saikalyankanika/sonar-cli', ttyEnabled: true, 
                                          resourceRequestMemory: '64Mi', resourceRequestCpu: '250m', 
                                          resourceLimitMemory: '128Mi', resourceLimitCpu: '500m')
                    ]) {
                        node(POD_LABEL) {
                            container('sonar-cli') {
                                //checking out the code for sonar scan
                                checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])

                                //running the sonar scan
                                withCredentials([string(credentialsId: 'sonar_key', variable: 'sonar')]) {
                                    sh "sonar-scanner -Dsonar.login=${sonar}"
                                    // some block
                                }

                            }
                        }
}
}