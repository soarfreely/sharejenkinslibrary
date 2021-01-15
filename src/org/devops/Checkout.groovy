package org.devops

//代码检出
def checkoutCode(srcUrl, credentialsId, tag) {
    //delete 'origin/'
    if (tag.startsWith('origin/')){
        branchName = tag.minus("origin/")
    }

    checkout([
        $class: 'GitSCM', branches: [
            [name: "${tag}"]
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