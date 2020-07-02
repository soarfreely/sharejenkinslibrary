package org.devOps

//邮件内容
def email(status, toEmail){
    def changeString = getChangeString()

    emailext body: """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
            </head>
            <body leftmargin="8" marginwidth="0" topmargin="8" marginheight="4" offset="0">
            <img src="https://pp.qn.img-space.com/202006/13/d530fd2b0c93178e668af0ef58383208.jpg?imageView2/2/w/200/q/75/ignore-error/1/">
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
                    <td><br />
                        <b><font color="#0B610B">提交注释</font></b>
                    </td>
                </tr>
                <tr>
                    <td>
                        <ul>
                            ${changeString}
                        </ul>
                    </td>
                </tr>
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
                changeString += " <li> - ${truncatedMsg} [${entry.author} ${commitTime}]</li>"
            }
        }
        if (!changeString) {
            changeString = "<li> - No new changes </li>"
        }

        return (changeString)
}
