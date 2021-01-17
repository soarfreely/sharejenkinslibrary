package org.devops

import groovy.json.JsonSlurper

/**
 * 获取分支详情
 *
 * @param repo 仓库名称
 * @param branch　分支名称
 * @return
 */
def branchDetail(repo, branch) {
    // https://api.github.com/repos/soarfreely/7fw/branches/7fw
    // api: /repositories/{repo_name}/tags/{tag}
    //    repo_name=library/share_libs
    //    tag=v0104
    // curl -X GET "http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104" -H "accept: application/json" -H "X-Xsrftoken: ae8DKqh1I88mE6T50ajKbrFGZkrCzS8Z"
    // http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104
    Object result = null
    String url = "https://api.github.com/repos/soarfreely/${repo}/branches/${branch}"
    try {
        def response = httpRequest contentType: 'APPLICATION_JSON',
                httpMode: "GET",
                url: url

        result = (new JsonSlurper()).parseText(response.content)
    } catch(Exception e) {
        print("branchDetail异常信息,分支${branch}可能不存在.详细信息:${e}")
    }

//    由 JsonSlurper 在表面下使用的 LazyMap 类经常是这个和许多其他问题的根源 .
//    在当前版本中找到的 JsonSlurper 的旧版本为 JsonSlurperClassic ，
//    出于这些目的使用常规的，可序列化的Java HashMap ，因此不容易出现相同类型的问题 .

    print((HashMap)result)
    print(((HashMap)result).get('name', 'defaultName'))

    return (HashMap)result
}