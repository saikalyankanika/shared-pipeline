# shared-pipeline

### prerequisites to use this library :
- adding the library in global system settings in jenkins 
- docker destop needed to be installed on your machine 




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