package org.devops

import groovy.json.JsonSlurper


def branchDetail(repo, branch) {
    // https://api.github.com/repos/soarfreely/7fw/branches/7fw
    // api: /repositories/{repo_name}/tags/{tag}
    //    repo_name=library/share_libs
    //    tag=v0104
    // curl -X GET "http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104" -H "accept: application/json" -H "X-Xsrftoken: ae8DKqh1I88mE6T50ajKbrFGZkrCzS8Z"
    // http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104
    Object result = null
    url = "https://api.github.com/repos/soarfreely/${repo}/branches/${branch}"
    try {
        def response = httpRequest contentType: 'APPLICATION_JSON',
                httpMode: "GET",
                url: url

        result = (new JsonSlurper()).parseText(response.content)
    } catch(Exception e) {
        print("imageDetail异常信息:${e}")
    }

    print(result.getProperties().get('digest'))
    print(result.getProperties())

    return result
}