package org.devops

//代码检出
def checkoutCode(srcUrl, credentialsId, branchName, tagName = null) {
    def tool = new Tools();

    //delete 'origin/'
    if (branchName.startsWith('origin/')){
        branchName=branchName.minus("origin/")
    }

    if(tagName == null){
        pathName = "*/${branchName}"
        tool.printMsg("branch_name:　${pathName}")

    } else {
        pathName = "refs/tags/${tagName}"
        tool.printMsg("tag_name:　${pathName}")
    }

    printMsg("${srcUrl}")

    checkout([
        $class: 'GitSCM', branches: [
            [name: "${pathName}"]
        ],
        doGenerateSubmoduleConfigurations: false,
        extensions: [],
        submoduleCfg: [],
        userRemoteConfigs: [
            [
                credentialsId: "${credentialsId}",
                url: "${srcUrl}"
            ]
        ],
        description: 'Select your branch or tag.'
    ])
}