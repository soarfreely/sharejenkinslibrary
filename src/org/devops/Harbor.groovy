package org.devops

def tagDetail () {
    // api: /repositories/{repo_name}/tags/{tag}
//    repo_name=library/share_libs
//    tag=v0104

    // curl -X GET "http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104" -H "accept: application/json" -H "X-Xsrftoken: ae8DKqh1I88mE6T50ajKbrFGZkrCzS8Z"

    // http://39.100.108.229/api/repositories/library%2Fshare_libs/tags/v0104
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

    def states = jsonParse(response.content)
    println states
}