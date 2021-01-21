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
def deploy(jenkins2serverCredentialsId, imageRepoUri, domain, tagName, nginxProxyPort) {

//    def parallelDeploy = [:]
//
//    for (int i = 0; i < hosts.size(); i++) {
//        def ip = hosts[i];
//        parallelDeploy["deploy-task-${ip}"] = {
//            sh """
//                ssh -p 22 root@${ip} "rm -rf /dockerfiles-${project}-${run_env}-${image_hash}"

//                echo "PHP_FPM_IMAGE=${env.DOCKER_REPO}/${docker_namespace}/crs-php-${project}-${run_env}-${image_hash}:${tag}" > dockerfiles/.env
//
//                echo "ENTERPRISE_HASH=${enterprise_hash}" >> dockerfiles/.env
//                echo "PROJECT=${project}" >> dockerfiles/.env
//                echo "RUN_ENV=${run_env}" >> dockerfiles/.env
//                echo "DOMAIN=${domain}" >> dockerfiles/.env
//                echo "SERVICE_NAME=${service_name}" >> dockerfiles/.env
//                echo "IMAGE_HASH=${image_hash}" >> dockerfiles/.env

//                ssh -v -p 22 root@${ip} "rm -rf /dockerfiles-${project}-${run_env}-${image_hash}"
//
//                scp -r dockerfiles root@${ip}:/dockerfiles-${project}-${run_env}-${image_hash}
//
//                ssh -p 22 root@${ip} "cd /dockerfiles-${project}-${run_env}-${image_hash} && sed -i '3s/php-fpm/php-fpm-${project}-${image_hash}/g' docker-compose.yml && sed -i '18s/default/default-${run_env}/g' docker-compose.yml"
//
//                ssh -p 22 root@${ip} "mkdir -p /data/www/${domain}/${image_hash}"
//
//                ssh -p 22 root@${ip} "chown -R 1000:1000 /data/www/${domain}/${image_hash}"
//
//                ssh -p 22 root@${ip} "cd /dockerfiles-${project}-${run_env}-${image_hash} && docker-compose up -d"
//
//                ssh -p 22 root@${ip} "chmod +x /dockerfiles-${project}-${run_env}-${image_hash}/kill-container.sh && /dockerfiles-${project}-${run_env}-${image_hash}/kill-container.sh ${env.DOCKER_REPO}/${docker_namespace}/crs-php-${project}-${run_env}-${image_hash} ${tag}"
//
//                ssh -v -p 22 root@${ip} "rm -rf /dockerfiles-${project}-${run_env}-${image_hash}"
//            """
//        }
//    }
//
//    parallel parallelDeploy

    String targetIp = '39.100.108.229'
    sshagent([jenkins2serverCredentialsId]) {
        sh """
            ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a && pwd
            ls -al
            
            ssh root@${targetIp} -tt "docker login -u admin -p ali229-Harbor ${imageRepoUri}"
            ssh root@${targetIp} -tt "docker pull ${imageRepoUri}/${domain}:${tagName}"
            """
    }

    killContainers(targetIp, domain)

    sshagent([jenkins2serverCredentialsId]) {
        sh """
            ssh root@${targetIp} -tt "docker run --name ${domain} -p ${nginxProxyPort}:80 -d ${imageRepoUri}/${domain}:${tagName}"
            ssh root@${targetIp} -tt "docker rmi -f ${imageRepoUri}/${domain}:${tagName}"
        """
    }
}

def killContainers(targetIp, domain) {
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

//def judge() {
//    # 查看进程是否存在
//    exist=`docker inspect --format '{{.State.Running}}' ${containerName}`
//    if [ "${exist}" != "true" ]; then
//    docker start ${containerName}
//    #记录日志
//    echo "${now} 重启docker容器，容器名称：${containerName}" >> /opt/docker_log/docker_monitor.log
//    fi
//}
//            if (`docker inspect --format "{{.State.Running}}" ${domain}`) {
//                docker rm -f ${domain}
//            }
//soar@soar:~/Desktop$ docker inspect --format '{{.State.Running}}' es123 >> /dev/null
//Error: No such object: es123
//soar@soar:~/Desktop$ docker inspect --format '{{.State.Running}}' es123 &> /dev/null
//soar@soar:~/Desktop$ docker inspect --format '{{.State.Running}}' es &> /dev/null
