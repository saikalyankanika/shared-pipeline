
def call(String repourl){


    env.ec2host = "ec2-54-146-53-101.compute-1.amazonaws.com"

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

    stage('Scan Docker Image') {
        
        // Run Trivy to scan the Docker image for vulnerabilities
        imagescan(repourl)
        
    }

}


