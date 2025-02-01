def call(String repourl){

    podTemplate(containers: [
                        containerTemplate(name: 'sonar-cli', image: 'saikalyankanika/sonar-scanner-cli:latest', ttyEnabled: true, 
                                          resourceRequestMemory: '64Mi', resourceRequestCpu: '250m', 
                                          resourceLimitMemory: '128Mi', resourceLimitCpu: '500m')
                    ]) {
                        node(POD_LABEL) {
                            container('sonar-cli') {
                                //checking out the code for sonar scan inside /usr/src folder
                                // dir('/usr/src/') {
                                sh "git clone ${repourl} repo"
                                // checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])

                            //running the sonar scan
                                withCredentials([string(credentialsId: 'sonar_key', variable: 'sonar')]) {
                                    sh "cd repo/ && sonar-scanner -Dsonar.login=${env.sonar}"

                                    }
                                        
                                    // }

                            }
                        }
}
}