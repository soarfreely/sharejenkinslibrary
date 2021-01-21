package org.devops

import groovy.json.JsonSlurper
import groovy.json.JsonSlurperClassic

/**
 * Harbor api: /repositories/{repo_name}/tags/{tag}
 * 镜像详情
 *
 * @return
 */
def imageDetail (api, basicAuth) {
    // api: /repositories/{repo_name}/tags/{tag}
//    repo_name=library/share_libs
//    tag=v0104
    // curl -X GET "http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104" -H "accept: application/json" -H "X-Xsrftoken: ae8DKqh1I88mE6T50ajKbrFGZkrCzS8Z"
    // http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104
    Object result = null
    try {
        def response = httpRequest contentType: 'APPLICATION_JSON',
                httpMode: "GET",
                customHeaders: [
                        [name: "Authorization", value: "Basic ${basicAuth}"]
                ],
                url: api

        result = (new JsonSlurperClassic()).parseText(response.content)
    } catch(Exception e) {
        print("imageDetail异常信息,tag可能不存在.详细信息:${e}")
    }

    print(result)
    print(((HashMap)result).get('name', false))

    return (HashMap)result
}

def httpGet() {
//    httpMode: "POST",
//    customHeaders: [
//            [name: "Authorization", value: "Basic xskjasdjkf="]
//    ],
//    requestBody: "key=key1&value=value1&key2=value",
//    url: ''
    String encodedAuthString = "Basic " + ("admin:ali229-Harbor".bytes.encodeBase64().toString())
    def response = httpRequest contentType: 'APPLICATION_JSON',
            httpMode: "GET",
            customHeaders: [
                [name: "Authorization", value: encodedAuthString]
            ],
            url: "http://39.100.108.229/api/repositories/library/share_libs/tags/v0104"
    println response.status
    println response.content

    def states = (new JsonSlurper()).parseText(response.content)
//    def states = jsonParse(response.content)
    println states
}