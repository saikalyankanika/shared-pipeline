def call(String repourl){
//     podTemplate(containers: [
//     containerTemplate(name: 'dind', image: 'docker:20.10-dind', privileged: true, command: 'dockerd-entrypoint.sh', args: '--host=tcp://0.0.0.0:2375 --host=unix:///var/run/docker.sock', 
//                       envVars: [
//                           envVar(key: 'DOCKER_TLS_CERTDIR', value: '')
//                       ],
//                       resourceRequestMemory: '512Mi', resourceRequestCpu: '500m', 
//                       resourceLimitMemory: '1024Mi', resourceLimitCpu: '1')
// ], volumes: [
//     hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
// ]) {
//     node(POD_LABEL) {
//         container('dind') {
//             // Ensure Docker is running
            // sh 'while(! docker info > /dev/null 2>&1); do sleep 1; done'

            

            // sshagent(['ec2agent']) {
            //     env.docker_artifactory = "registry.gitlab.com/test1773704/"
            //     // checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])

            //     echo sh(script: "rm -rf ${env.WORKSPACE}/*", returnStdout: true)

            //     echo sh(script:"git clone ${repourl} repo_now", returnStdout: true)

            //     echo sh(script: "ls -al", returnStdout: true)

            // //login to gitlab docker
            //     // withCredentials([usernamePassword(credentialsId: 'github', usernameVariable: 'GITLAB_USER', passwordVariable: 'GITLAB_PASS')]) {
            //     //     sh "docker login -u ${env.GITLAB_USER} -p ${env.GITLAB_PASS} registry.gitlab.com"
            //     // }

            // // Running commands inside the DinD container
            //     echo sh(script: "cd repo_now/ && docker build -t saikalyankanika/${env.JOB_NAME}:${env.version} .", returnStdout: true)

            //     echo sh(script: "cd repo_now/ && docker push saikalyankanika/${env.JOB_NAME}:${env.version}", returnStdout: true)


            // }

            sshagent(['ec2agent']) {
                script {
                    // Cleaning up workspace on the remote EC2 instance 
                    sh "ssh -o StrictHostKeyChecking=no ec2-user@${env.ec2host} 'rm -rf repo_now'"

                    // Cloning repository on the remote EC2 instance
                    sh "ssh ec2-user@${env.ec2host} 'git clone ${repourl} repo_now'"

                    // Listing files on the remote EC2 instance
                    sh "ssh ec2-user@${env.ec2host} 'ls -al repo_now'"

                    // Logging into GitLab Docker registry on the remote EC2 instance
                    // withCredentials([usernamePassword(credentialsId: 'gitlab', usernameVariable: 'GITLAB_USER', passwordVariable: 'GITLAB_PASS')]) {
                    //     sh "ssh ec2-user@your-ec2-ip 'echo ${env.GITLAB_PASS} | docker login -u ${env.GITLAB_USER} --password-stdin registry.gitlab.com'"
                    // }

                    // Building Docker image on the remote EC2 instance
                    sh "ssh ec2-user@${env.ec2host} 'cd repo_now && docker build -t saikalyankanika/${env.JOB_NAME}:${env.version} .'"

                    // Pushing Docker image to GitLab Docker registry from the remote EC2 instance
                    sh "ssh ec2-user@${env.ec2host} 'cd repo_now && docker push saikalyankanika/${env.JOB_NAME}:${env.version}'"
                }
            }


            


        // }
    // }
// }

}