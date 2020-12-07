node {
    
    stage('Deploy to PROD instance') {
        script {
            // Define Variable
             def USER_INPUT = input(
                    message: 'Please, approve deployment and choose which deployment you need to do?',
                    parameters: [
                            [$class: 'ChoiceParameterDefinition',
                             choices: ['Deploy containers for the first time','Update existing containers'].join('\n'),
                             name: 'input',
                             description: 'Menu - select box option']
                    ])

            echo "${USER_INPUT}"

            if( "${USER_INPUT}" == "Deploy containers for the first time"){
                sshagent (credentials: ['stage']) {
                sh 'ssh -o StrictHostKeyChecking=no -l inception 35.217.5.220 "git clone https://github.com/maksym-butusov/videoshop && cd videoshop && docker-compose -f docker-compose.yml up -d"'
                }
            } else {
                sshagent (credentials: ['stage']) {
                sh 'ssh -o StrictHostKeyChecking=no -l inception 35.217.5.220 "cd videoshop && docker-compose -f docker-compose.yml down && docker system prune -a -f && cd .. && sudo rm -rf videoshop/ && git clone https://github.com/maksym-butusov/videoshop && cd videoshop && docker-compose -f docker-compose.yml up -d"'
                }
            }
        }
    }
}
