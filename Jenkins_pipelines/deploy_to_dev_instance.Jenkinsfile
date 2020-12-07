
node{

    stage('Deploy containers to Dev instance') {
   sshagent (credentials: ['stage']) {
    sh 'ssh -o StrictHostKeyChecking=no -l inception 35.217.57.162 "sudo rm -rf videoshop/ && git clone https://github.com/maksym-butusov/videoshop && cd videoshop && docker-compose -f docker-compose.yml up -d"'
 }
 }

    stage('Sleep-waiting APP') {
    sh "sleep 3m"
}

    stage('Check Availability APP page') {
          timeout(time: 10, unit: 'SECONDS') {
               stage('Check Availability APP') {
          script {
               waitUntil {
                    try {
                         sh "curl -s --head  --request GET  http://35.217.57.162:8080 | grep '200'"
                         return true
                    } catch (Exception e) {
                         return false
                    }
               }
          }
               }
          }
     }
     
    sshagent (credentials: ['stage']) {
    sh 'ssh -o StrictHostKeyChecking=no -l inception 35.217.57.162 "cd videoshop && docker-compose -f docker-compose.yml down && docker system prune -a -f"'
}

}
