package org.devOps

String gitServer
List credentialsId


//封装HTTP请求
def httpReq(reqType, reqUrl, reqBody){
    gitServer = this.gitServer
//     credentialsId = this.credentialsId
//     withCredentials([string(credentialsId: credentialsId, variable: 'gitlabToken')]) {
//     println(credentialsId)
//     println("gitlabToken")

      result = httpRequest customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "JYGwuuBfZopiCo3qk3nH"]],
                httpMode: reqType,
                contentType: "APPLICATION_JSON",
                consoleLogResponseBody: true,
                ignoreSslErrors: true,
                requestBody: reqBody,
                url: "${gitServer}/${reqUrl}"
                //quiet: true
//     }
    return result
}

//提交-更新文件内容
def updateRepositoryFile(projectId, filePath, fileContent, branch){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch": "${branch}","encoding":"base64", "content": "${fileContent}", "commit_message": "update file: ${filePath}"}"""
    response = httpReq('PUT', apiUrl, reqBody)
    println(response)
}

//获取文件内容
def getRepositoryFile(projectId,filePath){
    apiUrl = "projects/${projectId}/repository/files/${filePath}/raw?ref=master"
    response = httpReq('GET',apiUrl,'')
    return response.content
}

//创建仓库文件
def createRepositoryFile(projectId,filePath,fileContent){
    apiUrl = "projects/${projectId}/repository/files/${filePath}"
    reqBody = """{"branch": "master","encoding":"base64", "content": "${fileContent}", "commit_message": "create a new file"}"""
    response = httpReq('POST',apiUrl,reqBody)
    println(response)
}


//更改提交状态
def changeCommitStatus(projectId,commitSha,status){
    commitApi = "projects/${projectId}/statuses/${commitSha}?state=${status}"
    response = httpReq('POST',commitApi,'')
    println(response)
    return response
}

//获取工程ID
def getProjectID(credentialsId, projectName){
    projectApi = "projects?search=${projectName}"
    println("projectApi:${projectApi}")

    response = httpReq('GET', projectApi, '')
    def result = readJSON text: """${response.content}"""

    println(result)
    for (repo in result){
       // println(repo['path_with_namespace'])
        if (repo['path'] == "${projectName}"){

            repoId = repo['id']

            println("工程ID:${repoId}")
        }
    }
    return repoId
}

//删除分支
def deleteBranch(projectId,branchName){
    apiUrl = "/projects/${projectId}/repository/branches/${branchName}"
    response = httpReq("DELETE",apiUrl,'').content
    println(response)
}

//创建分支
def createBranch(projectId,refBranch,newBranch){
    try {
        branchApi = "projects/${projectId}/repository/branches?branch=${newBranch}&ref=${refBranch}"
        response = httpReq("POST",branchApi,'').content
        branchInfo = readJSON text: """${response}"""
    } catch(e){
        println(e)
    }  //println(branchInfo)
}

//创建合并请求
def createMr(projectId,sourceBranch,targetBranch,title,assigneeUser=""){
    try {
        def mrUrl = "projects/${projectId}/merge_requests"
        def reqBody = """{"source_branch":"${sourceBranch}", "target_branch": "${targetBranch}","title":"${title}","assignee_id":"${assigneeUser}"}"""
        response = httpReq("POST",mrUrl,reqBody).content
        return response
    } catch(e){
        println(e)
    }
}

//搜索分支
def searchProjectBranches(projectId,searchKey){
    def branchUrl =  "projects/${projectId}/repository/branches?search=${searchKey}"
    response = httpReq("GET",branchUrl,'').content
    def branchInfo = readJSON text: """${response}"""

    def branches = [:]
    branches[projectId] = []
    if(branchInfo.size() ==0){
        return branches
    } else {
        for (branch in branchInfo){
            //println(branch)
            branches[projectId] += ["branchName":branch["name"],
                                    "commitMes":branch["commit"]["message"],
                                    "commitId":branch["commit"]["id"],
                                    "merged": branch["merged"],
                                    "createTime": branch["commit"]["created_at"]]
        }
        return branches
    }
}

//允许合并
def acceptMr(projectId,mergeId){
    def apiUrl = "projects/${projectId}/merge_requests/${mergeId}/merge"
    httpReq('PUT',apiUrl,'')
}

// 初始化必要变量
def initVariable(gitServer, credentialsId) {
    this.gitServer = gitServer
    this.credentialsId = credentialsId
    println(this.gitServer)
    println(this.credentialsId)
}
