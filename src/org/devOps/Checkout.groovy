package org.devOps

//代码检出
def checkout(srcUrl, credentialsId, branchName, tagName = null) {
    //delete 'origin/'
    if (branchName.startsWith('origin/')){
        branchName=branchName.minus("origin/")
    }

    if(tagName == "null"){
        pathName = "*/${branchName}"
    }else{
        pathName = "refs/tags/${tagName}"
    }

    println("-------------")
    println("${branchName}")
    println("${srcUrl}")

    checkout([
        $class: 'GitSCM', branches: [
            [name: "${branchName}"]
        ],
        doGenerateSubmoduleConfigurations: false,
        extensions: [],
        submoduleCfg: [],
        userRemoteConfigs: [
            [
                credentialsId: "${credentialsId}",
                url: "${srcUrl}"
            ]
        ]
    ])
}