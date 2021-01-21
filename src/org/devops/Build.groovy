package org.devops

def build(imageRepoUri,newImageName, tagName) {
//    env.serviceName = "${JOB_NAME}".split("_")[0]
//    env.dockerUrl = '39.100.108.229'
//    env.newImageName = "nginx16phpfpm73/ali";

    def tool = new Tools();
    tool.printMsg("newImageName:${newImageName}--tagName:${tagName}")

    sshagent(['deepin-2-39']) {
        sh """
           echo '当前目录:'
           pwd
           docker login -u admin -p ali229-Harbor ${imageRepoUri}
           echo '私有镜像仓库登录成功'
           docker build --no-cache -f docker/Dockerfile -t ${imageRepoUri}/${newImageName}:${tagName} .
           sleep 1
           docker tag ${imageRepoUri}/${newImageName}:${tagName}  ${imageRepoUri}/${newImageName}:${tagName}
           sleep 1
           docker push ${imageRepoUri}/${newImageName}:${tagName}
        """
    }
//    withCredentials([usernamePassword(credentialsId: 'aliyun-registry-admin', passwordVariable: 'password', usernameVariable: 'username')]) {
//
//        // .env 的处理待完善
//
//        sh """
//           docker login -u ${username} -p ${password}  ${dockerUrl}
//
//           [ -f src/.env.${enterprise_name} ] && cp src/.env.${enterprise_name} src/.env
//           [ ! -f src/.env.${enterprise_name} ] && cp src/.env.${run_env} src/.env
//
//           docker build --no-cache -f docker/Dockerfile -t ${newImageName}:${tagName} .
//           sleep 1
//           docker push 39.100.108.229/library/${newImageName}:${tagName}
//           sleep 1
//           docker rmi ${newImageName}:${tagName}
//        """
//    }
}

// 打包
def tar(projectName, targetIp, credentialsId, tarName) {
    echo 'start tar ............'
    echo "start tar1 ${projectName}"
    echo "start tar2 ${targetIp}"
    echo "start tar3 ${credentialsId}"

    echo "start tar5 ${tarName}"

    sshagent(["${credentialsId}"]) {
        sh('ls -al')
        sh("rm -rf *.tar.gz && tar -zcvf ${tarName} ./* --exclude=./git")
        sh('ls -al')
//         sh """
//             ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a
//             echo 123
//             ls -al
//             ssh -o StrictHostKeyChecking=no -l root ${targetIp} uname -a && pwd
//             ls -al
//             scp ${tarName} root@${targetIp}:${dir}
//             echo 456
//             ssh root@${targetIp} -tt "ls -al && cd ${dir} && mkdir -p ${projectName} && tar zxvf ${tarName} -C ${projectName}"
//         """
    }
}