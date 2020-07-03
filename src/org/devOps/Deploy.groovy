package org.devOps

// IDC上传
def upload(domain, targetIp, credentialsId, phpSrc, runComposer, www, tarName) {
    def targetDir = "${www}/${domain}"
    def targetDirTmp = "${www}/${domain}_tmp"
    def srcDir = "${targetDir}/${phpSrc}"

    sshagent(["${credentialsId}"]) {
        sh('ls -al')
        sh """
            ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a && pwd
            ls -al
            ssh root@${targetIp} -tt  "mkdir -p ${targetDir} && ssh mkdir -p ${targetDirTmp}"
            scp ${tarName} root@${targetIp}:${targetDirTmp}
            ssh root@${targetIp} -tt "ls -al && cd ${targetDirTmp} && tar zxvf ${tarName} -C ${targetDir}"

            ssh root@${targetIp} -tt "cd ${srcDir} && pwd && rm -f *Jenkinsfile && ${runComposer} && composer install"
        """
    }
}

// 容器发布
def deploy() {
    sh """
        docker run
    """
}