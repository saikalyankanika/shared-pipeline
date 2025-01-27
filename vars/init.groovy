
def call(string repourl){

    
    stage('OWSAP-Dependency-check'){

        //checking out the cod e for dependency check
        checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/saikalyankanika/react-sample-ksk']])
        //running the dependency check
        dependencyCheck additionalArguments: '''--format HTML''', odcInstallation: 'DP-check', stopBuild: true
    }

    println("Hello from init.groovy")
    if(env.sonar_scan!=null){
    stage('Sonar Scan') {


}

}

}


