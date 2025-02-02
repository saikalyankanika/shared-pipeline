def call(String repourl){

    
        podTemplate(containers: [
                        containerTemplate(name: 'awscli-image', image: 'saikalyankanika/aws-provision-terraform-python', ttyEnabled: true, 
                                          resourceRequestMemory: '512Mi', resourceRequestCpu: '500m', 
                                          resourceLimitMemory: '1024Mi', resourceLimitCpu: '1')
                    ]) {
                        node(POD_LABEL) {
                            container('awscli-image') {
                                //checking out the code for sonar scan inside /usr/src folder
                                // dir('/usr/src/') {
                                sh "git clone ${repourl} repo"
                                // checkout changelog: false, poll: false, scm: scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: "${repourl}"]])

                                    withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'apoorva_creds', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                                        sh "aws configure set aws_access_key_id '${AWS_ACCESS_KEY_ID}'"
                                        sh "aws configure set aws_secret_access_key '${AWS_SECRET_ACCESS_KEY}'"
                                }

                                dir('repo/') {
                                    
                                    // sh (script: "aws iam create-role --role-name ecsTaskExecutionRole --assume-role-policy-document file://ecs-trust-policy.json", returnStdout: true)

                                    // //Attach the AWS managed policy for ECS task execution to the role
                                    // sh (script: "aws iam attach-role-policy --role-name ecsTaskExecutionRole --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy", returnStdout: true)

                                    // //Create ECS cluster

                                    // sh (script: "aws ecs create-cluster --cluster-name ksk-cluster", returnStdout: true)

                                    // sh (script: "aws ecs register-task-definition --cli-input-json file://task-definition.json", returnStdout: true)

                                    // sh (script: "aws ecs create-service --cluster ksk-cluster --service-name ksk-react-service --task-definition ksk-react-app --desired-count 1 --launch-type FARGATE --network-configuration 'awsvpcConfiguration={subnets=[subnet-0123456789abcdef0],securityGroups=[sg-0123456789abcdef0],assignPublicIp=ENABLED}'", returnStdout: true)



                                    sh """
                                if ! aws iam get-role --role-name ecsTaskExecutionRole >/dev/null 2>&1; then
                                    aws iam create-role --role-name ecsTaskExecutionRole --assume-role-policy-document file://ecs-trust-policy.json
                                    aws iam attach-role-policy --role-name ecsTaskExecutionRole --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
                                else
                                    echo "IAM role ecsTaskExecutionRole already exists"
                                fi
                                """

                                // Create ECS cluster
                                sh """
                                if ! aws ecs describe-clusters --clusters ksk-cluster --region ${AWS_REGION} >/dev/null 2>&1; then
                                    aws ecs create-cluster --cluster-name ksk-cluster
                                else
                                    echo "ECS cluster ksk-cluster already exists"
                                fi
                                """

                                // Register task definition
                                sh 'aws ecs register-task-definition --cli-input-json file://task-definition.json'

                                // Create ECS service
                                sh """
                                aws ecs create-service --cluster ksk-cluster --service-name ksk-react-service --task-definition ksk-react-app --desired-count 1 --launch-type FARGATE --network-configuration 'awsvpcConfiguration={subnets=[subnet-0123456789abcdef0],securityGroups=[sg-0123456789abcdef0],assignPublicIp=ENABLED}'
                                """

                                }
                                

                            }
                        }
    }

}