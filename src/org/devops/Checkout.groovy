package org.devops

//代码检出
def checkoutCode(srcUrl, credentialsId, branchOrTag) {
    //delete 'origin/'
    if (branchOrTag.startsWith('origin/')){
        branchOrTag = branchOrTag.minus("origin/")
    }

    checkout([
        $class: 'GitSCM', branches: [
            [name: "${branchOrTag}"]
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
