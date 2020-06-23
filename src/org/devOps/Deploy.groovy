package org.devOps

// IDC上传
def upload(projectName, targetIp, credentialsId, phpSrc, runComposer) {
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

            ssh root@${targetIp} -tt "cd ${dir}/${projectName}/${phpSrc} && pwd && ${runComposer} && composer install"
        """
    }
}

// 容器发布
def deploy() {
    sh """
        docker run
    """
}