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

// 容器发布
def deploy(newImageName, tagName) {
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

//    withCredentials([usernamePassword(credentialsId: 'aliyun-registry-admin', passwordVariable: 'password', usernameVariable: 'username')]) {
        sh """
            docker login -u admin -p ali229-Harbor  39.100.108.229
            docker pull 39.100.108.229/library/${newImageName}:${tagName}
            sleep 1
            docker run --name ${newImageName}-${tagName} -p 8080:80 -d ${newImageName}:${tagName}
        """
//    }
}