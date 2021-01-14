package org.devops

def build(tagName) {
    def tools = new Tools();

    tools.printMsg("构建上传镜像-start","green")
//    env.serviceName = "${JOB_NAME}".split("_")[0]
//    env.dockerUrl = '39.100.108.229'
//    env.newImageName = "nginx16phpfpm73/ali";

    sh """
           docker login -u admin -p ali229-Harbor  39.100.108.229
           
           [ -f src/.env.${enterprise_name} ] && cp src/.env.${enterprise_name} src/.env
           [ ! -f src/.env.${enterprise_name} ] && cp src/.env.${run_env} src/.env

           docker build --no-cache -f docker/Dockerfile -t ${newImageName}:${tagName} .
           sleep 1
           docker push 39.100.108.229/library/nginx16phpfpm73:1.0
           sleep 1
     """
    tools.PrintMes("构建上传镜像-end","green")

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