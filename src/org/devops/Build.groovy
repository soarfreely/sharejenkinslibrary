package org.devops

/**
 *  构建
 * @param imageRepoUri
 * @param newImageName
 * @param tagName
 * @return
 */
def build(imageRepoUri, newImageName, tagName) {
    def tool = new Tools();
    tool.printMsg("newImageName:${newImageName}--tagName:${tagName}")

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
       sleep 1
       docker rmi -f ${imageRepoUri}/${newImageName}:${tagName}
    """
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
    }
}