package org.devOps

// IDC上传
def upload(projectName, targetIp, credentialsId) {
    def dir = '/root/www'
    def tarName = "${projectName}.tar.gz"

    sshagent(["${credentialsId}"]) {
        sh('ls -al')
        sh """
            ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a && pwd
            ls -al
            scp ${tarName} root@${targetIp}:${dir}
            echo 456
            ssh root@${targetIp} -tt "ls -al && cd ${dir} && mkdir -p ${projectName} && tar zxvf ${tarName} -C ${projectName}"
        """
    }
}

// 容器发布
def deploy() {
    sh """
        docker run
    """
}