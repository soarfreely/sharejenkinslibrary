package org.devOps

def build() {
    tools.PrintMes("构建上传镜像","green")
    env.serviceName = "${JOB_NAME}".split("_")[0]
    env.dockerUrl = '39.100.108.229'
    env.newImageName = "${dockerUrl}/ali";

    withCredentials([usernamePassword(credentialsId: 'aliyun-registry-admin', passwordVariable: 'password', usernameVariable: 'username')]) {

       sh """
           docker login -u ${username} -p ${password}  ${dockerUrl}
           docker build -f ${dockerFiles} -t ${newImageName}:${tagName} .
           sleep 1
           docker push ${dockerFiles} -t ${newImageName}:${tagName}
           sleep 1
           docker rmi ${dockerFiles} -t ${newImageName}:${tagName}
        """
    }
}