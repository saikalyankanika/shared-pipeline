def call(String repourl){

    podTemplate(containers: [
                        containerTemplate(name: 'sonar-cli', image: 'saikalyankanika/sonar-scanner-cli:latest', ttyEnabled: true, 
                                          resourceRequestMemory: '256Mi', resourceRequestCpu: '500m', 
                                          resourceLimitMemory: '512Mi', resourceLimitCpu: '1')
                    ]) {
                        node(POD_LABEL) {
                            container('sonar-cli') {
                                //checking out the code for sonar scan inside /usr/src folder
                                // dir('/usr/src/') {
                                sh "git clone ${repourl} /usr/src/repo"
                                // checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])

                            //running the sonar scan
                                withCredentials([string(credentialsId: 'sonar_key', variable: 'sonar')]) {
                                    sh "cd /usr/src/repo/ && sonar-scanner -Dsonar.login=${env.sonar}"

                                    }
                                        
                                    // }

                            }
                        }
}
}