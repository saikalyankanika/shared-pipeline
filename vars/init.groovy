
def call(String repourl){

    // OSWAP Dependency Check
    stage('OWSAP-Dependency-check'){

        //checking out the cod e for dependency check
        checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])
        //running the dependency check
        // dependencyCheck additionalArguments: '''--format HTML''', odcInstallation: 'DP-check', stopBuild: true
    }

    // Sonar Scan
    if(env.sonarscan!=null){
        stage('Sonar Scan') {
            sonar(repourl)
        }
}

    // Image Building
    if(env.imagescan!=null){
        stage('Image Building') {
            imagebuilding(repourl)
        }
    }

}


