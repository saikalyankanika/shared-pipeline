def call(String repourl){
    podTemplate(containers: [
    containerTemplate(name: 'dind', image: 'docker:20.10-dind', privileged: true, command: 'dockerd-entrypoint.sh', args: '--host=tcp://0.0.0.0:2375 --host=unix:///var/run/docker.sock', 
                      envVars: [
                          envVar(key: 'DOCKER_TLS_CERTDIR', value: '')
                      ],
                      resourceRequestMemory: '512Mi', resourceRequestCpu: '500m', 
                      resourceLimitMemory: '1024Mi', resourceLimitCpu: '1')
], volumes: [
    hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
    node(POD_LABEL) {
        container('dind') {
            // Ensure Docker is running
            sh 'while(! docker info > /dev/null 2>&1); do sleep 1; done'

            env.docker_artifactory = "registry.gitlab.com/test1773704/"

            // checking out the code:
            checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])

            //login to gitlab docker
            withCredentials([usernamePassword(credentialsId: 'github', usernameVariable: 'GITLAB_USER', passwordVariable: 'GITLAB_PASS')]) {
                sh "docker login -u ${env.GITLAB_USER} -p ${env.GITLAB_PASS} registry.gitlab.com"
            }

            // Running commands inside the DinD container
            echo sh(script: "docker build -t registry.gitlab.com/test1773704/${env.JOB_NAME}:${env.version}", returnStdout: true)

            echo sh(script: "docker push registry.gitlab.com/test1773704/${env.JOB_NAME}:${env.version}", returnStdout: true)



        }
    }
}

}