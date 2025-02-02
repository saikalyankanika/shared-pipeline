def call(String repourl){


            // Run Trivy to scan the Docker image for vulnerabilities

        sshagent(['ec2agent']) {
            script {
                sh "ssh ec2-user@${env.ec2host} 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image saikalyankanika/${env.JOB_NAME}:${env.version}'"
            }
        }

}