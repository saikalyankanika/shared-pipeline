# shared-pipeline

### prerequisites to use this library :
- adding the library in global system settings in jenkins 
- docker destop needed to be installed on your machine 
- an EC2 machine needs to be spinned up on aws account and this needs to be added in the ssh connection in jenkins system configuration (ec2 connection details needs to be updated):
- Dashboard>Manage Jenkins>System

![ssh setup](./images/SSH-setup.PNG)



- needs to install docker and git on ec2 machine .

- needs to setup sonar , i have used sonarqube cloud platform and given that properties in my application

- needs to add required credentials in jenkins


### stages that the pipeline undergoes:

```
CI-Part:
    - Initialization stage
    - OSWAP dependencies pull
    - sonar check
    - build and push
    - image build and push
    - image scanning
CD-Part:
    - predeployment checks
    - deployment 

```