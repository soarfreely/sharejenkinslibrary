package org.devops

//代码检出
def checkoutCode(srcUrl, credentialsId, branchName, tagName = null) {
    //delete 'origin/'
    if (branchName.startsWith('origin/')){
        branchName=branchName.minus("origin/")
    }

    if(tagName == "null"){
        pathName = "*/${branchName}"
    }else{
        pathName = "refs/tags/${tagName}"
    }

    println("code path name:　${pathName}")
    println("${srcUrl}")

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