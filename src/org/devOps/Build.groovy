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

// 打包
def tar(projectName, dir) {
    dir = '/home/www'
    echo 'start tar ............'
    sshagent(['jenkins--ssh-deepin']) {
        sh('ls -al')
        sh("tar zvf ${projectName}.tar ./* --exclude=./git")
        sh '''
            ssh -o StrictHostKeyChecking=no -l root 172.17.0.5 uname -a
            echo 123
            scp ${projectName}.tar 172.17.0.5:/home/www
            echo 456
            ssh root@172.17.0.5 -tt "ls -al && cd /home/www && tar zxvf ${projectName} -C ${dir}"
       '''
    }
}