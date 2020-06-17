package org.devOps

//邮件内容
def Email(status, toEmail){
    emailext body: """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
            </head>
            <body leftmargin="8" marginwidth="0" topmargin="8" marginheight="4" offset="0">
            <img src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592038363060&di=4a0fe582c114a8d5afe4aef967e2ca2e&imgtype=0&src=http%3A%2F%2Fdownload.img.dns4.cn%2Fpic%2F192713%2Fp18%2F20171214154800_8834_zs_sy.jpeg">
            <table width="95%" cellpadding="0" cellspacing="0" style="font-size: 11pt; font-family: Tahoma, Arial, Helvetica, sans-serif">
                <tr>
                    <td><br />
                        <b><font color="#0B610B">构建信息</font></b>
                    </td>
                </tr>
                <tr>
                    <td>
                        <ul>
                            <li>项目名称：${JOB_NAME}</li>
                            <li>构建编号：${BUILD_ID}</li>
                            <li>构建状态: ${status} </li>
                            <li>项目地址：<a href="${BUILD_URL}">${BUILD_URL}</a></li>
                            <li>构建日志：<a href="${BUILD_URL}console">${BUILD_URL}console</a></li>
                        </ul>
                    </td>
                </tr>
                <tr>
            </table>
            </body>
            </html>  """,
            subject: "Jenkins-${JOB_NAME}项目构建信息 ",
            to: toEmail
        
}
/**
* currentBuild.changeSets{
      items[{
          msg //提交注释
          commitId //提交hash值
          author{ //提交用户相关信息
              id
              fullName
          }
          timestamp
          affectedFiles[{ //受影响的文件列表
              editType{
                  name
              }
              path: "path"
          }]
          affectedPaths[// 受影响的目录，是个Collection<String>
              "path-a","path-b"
          ]
      }]
  }
*/
// 提交注释
def getChangeString() {
        def changeString = ""
        def MAX_MSG_LEN = 20
        def changeLogSets = currentBuild.changeSets
        for (int i = 0; i < changeLogSets.size(); i++) {
            def entries = changeLogSets[i].items
            for (int j = 0; j < entries.length; j++) {
                def entry = entries[j]
                truncatedMsg = entry.msg.take(MAX_MSG_LEN)
                commitTime = new Date(entry.timestamp).format("yyyy-MM-dd HH:mm:ss")
                changeString += " - ${truncatedMsg} [${entry.author} ${commitTime}]\n"
            }
        }
        if (!changeString) {
            changeString = " - No new changes"
        }
        return (changeString)
}
