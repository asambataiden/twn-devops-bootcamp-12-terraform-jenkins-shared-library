#!/user/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker image..."
        script.sh "docker build -t $imageName ."
        }

    def dockerLogin() {
        script.withCredentials([
            script.usernamePassword(
                credentialsId: 'docker-hub-repo-asambataiden',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASSWORD'
            )
        ]) {
            script.sh '''
                set -eu

                printf '%s' "$DOCKER_PASSWORD" \
                | docker login \
                    --username "$DOCKER_USER" \
                    --password-stdin
            '''
        }
    }

    def dockerPush(String imageName) {
        script.sh "docker push $imageName"
    }
}
