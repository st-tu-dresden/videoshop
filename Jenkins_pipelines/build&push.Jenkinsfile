/***** STAGE AREA STEP ****/    
node{

    environment {
    registry = "incepti0n/videoshop"
    registryCredential = 'dockerhub'
    
    }   

    stage ('Clone Repository'){
        checkout scm
    }
    
    stage('Exec Maven ang get .jar'){
        sh "./mvnw clean package"
    }
      
    def app_videoshop
    
    stage('Build image') {
    /* This builds the actual image; synonymous to
     * docker build on the command line */

    app_videoshop = docker.build("incepti0n/videoshop", "-f Dockerfile .")
                       
    }
    
    /***** PUSH IMAGE STEP ****/     
    stage('Push image') {
    /* Finally, we'll push the image with two tags:
     * First, the incremental build number from Jenkins
     * Second, the 'win' tag.
     * Pushing multiple tags is cheap, as all the layers are reused. */
    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
        app_videoshop.push("${env.BUILD_NUMBER}")
        app_videoshop.push("latest")
    }
    }
        
}
