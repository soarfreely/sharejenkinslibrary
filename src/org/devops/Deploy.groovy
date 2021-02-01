package org.devops

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
            ssh root@${targetIp} -tt  "mkdir -p ${targetDir} && mkdir -p ${targetDirTmp}"
            scp ${tarName} root@${targetIp}:${targetDirTmp}
            ssh root@${targetIp} -tt "ls -al && cd ${targetDirTmp} && tar zxvf ${tarName} -C ${targetDir}"
            ssh root@${targetIp} -tt "cd ${targetDir} && rm -f *Jenkinsfile "
            ssh root@${targetIp} -tt "cd ${srcDir} && pwd && ${runComposer} && composer install"
        """
    }
}

/**
 * 容器部署
 *
 * @param jenkins2serverCredentialsId
 * @param imageRepoUri
 * @param domain 域名作为容器名称
 * @param tagName tag
 * @return
 */
def deploy(jenkins2serverCredentialsId, imageRepoUri, domain, tagName, targetIp, nginxProxyPort) {

    // TODO 临时修改（因为镜像服务器和业务服务器是同一个）
    String imageRepoUriTmp = '127.0.0.1/library'
    imageRepoUri = imageRepoUriTmp

    sshagent([jenkins2serverCredentialsId]) {
        sh """
            ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a && pwd
            ls -al
            
            ssh root@${targetIp} -tt "docker login -u admin -p ali229-Harbor ${imageRepoUri}"
            ssh root@${targetIp} -tt "docker pull ${imageRepoUri}/${domain}:${tagName}"
            """
    }

    killContainers(jenkins2serverCredentialsId, targetIp, domain)

    sshagent([jenkins2serverCredentialsId]) {
        sh """
            ssh root@${targetIp} -tt "docker run --name ${domain} -p ${nginxProxyPort}:80 -d ${imageRepoUri}/${domain}:${tagName}"
            ssh root@${targetIp} -tt "docker rmi -f ${imageRepoUri}/${domain}:${tagName}"
        """
    }
}

/**
 * kill 掉容器
 * @param jenkins2serverCredentialsId
 * @param targetIp
 * @param domain
 * @return
 */
def killContainers(jenkins2serverCredentialsId, targetIp, domain) {
    print("debug :killContainers::killContainers")

    try {
        sshagent([jenkins2serverCredentialsId]) {
            sh """
                ssh root@${targetIp} -tt "docker rm -f ${domain}"
            """
        }
    } catch (Exception e) {
        print("killContainers:")
        print(e)
    }
}
