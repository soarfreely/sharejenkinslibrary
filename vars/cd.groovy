import org.devops.Build
import org.devops.Checkout
import org.devops.Deploy
import org.devops.Email
import org.devops.Github
import org.devops.Tools
import org.devops.Harbor

// 容器部署

def call(Closure body) {
     body()

     def tool = new Tools()
     def email = new Email()
     def checkout = new Checkout()
     def build = new Build()
     def deploy = new Deploy()
     def harbor = new Harbor()
     def github = new Github()

     def targetIp = body.targetIp
     def toEmail = body.toEmail
     def runComposer = body.runComposer
     def phpSrc = body.phpSrc
     def repositoryPath = body.repositoryPath
     def jenkins2repositoryCredentialsId = body.jenkins2repositoryCredentialsId
     def jenkins2serverCredentialsId = body.jenkins2serverCredentialsId
     def www = body.www
     def repositoryName = body.repositoryName
     def domain = body.domain
     def tarName = "${domain}_${BUILD_ID}.tar.gz"
     def submitter = "gavin, admin"


     tool.println("domain:${domain}")
     def generateTag = tool.generateTag(domain)
     String imageRepoUri = '39.100.108.229/library';
     // Harbor仓库镜像详情接口
     String basicAuth = "Basic " + ("admin:ali229-Harbor".bytes.encodeBase64().toString())

     tool.printMsg("Gavin' jenkinsfile share library", 'green')

     tool.printMsg("是否执行composer:${runComposer}", 'green')
     tool.printMsg("代码所在目录:${phpSrc}", 'green')
     tool.printMsg("params:${params}", 'green')
     tool.printMsg("代码仓库:${repositoryPath}", 'green')
     tool.printMsg("jenkins2repository凭据:${jenkins2repositoryCredentialsId}", 'green')
     tool.printMsg("jenkins2server凭据:${jenkins2serverCredentialsId}", 'green')
     tool.printMsg("目标服务器:${targetIp}", 'green')
     tool.printMsg("负责人邮箱:${toEmail}", 'green')

     // jenkins 工作目录
     pipeline {
          agent {
               node {
                    label "master" // 指定运行节点的标签或者名称
               }
          }
          // 指定运行选项（可选）
          options {
               timestamps() // 日志会有日志
               skipDefaultCheckout() // 删除隐藏checkout scm 语句
               disableConcurrentBuilds() // 禁止并行（根据实际情况）
               timeout(time:1, unit:"HOURS") // 流水线超时设置
               // 表示保留6次构建历史
               buildDiscarder(logRotator(daysToKeepStr:'1', numToKeepStr:'6', artifactDaysToKeepStr:'2', artifactNumToKeepStr:'5'))
          }

          // 参数
          parameters {
               string(name: 'tag', defaultValue: 'tag', description: 'Please enter the code tag to be built')
          }

          stage ("Deploy") {
               steps {
                    timeout(time:20, unit:"MINUTES") {
                         script {
                              tool.printMsg('开始:拉取业务镜像&部署', 'green')
                              tool.printMsg("Deploy-debug:${tag}", 'green')

                              def imageResponse = harbor.imageDetail("http://39.100.108.229/api/repositories/library/${domain}/tags/${tag}", basicAuth)
                              if (!(boolean)imageResponse.get('name', null)) {
                                   deploy.deploy(imageRepoUri, domain, tag)
                              }
////                            deploy.deploy(domain, targetIp, jenkins2serverCredentialsId, phpSrc, runComposer, www, tarName)
                              tool.printMsg("结束:拉取业务镜像&部署", 'green')
                         }
                    }
               }
          }

          post { // 构建后的操作
               always {
                    script {
                         println("always")
//                    harbor.httpGet()
                         println("always")
                    }
               }

               // currentBuild 全局变量，description 构建描述
               success {
                    script {
                         String status = '构建成功'
                         currentBuild.description = "\n ${status}!"
                         email.email(status, toEmail)

                         tool.printMsg("删除业务镜像")
                         sh """
                            docker rmi -f ${imageRepoUri}/${domain}:${generateTag}
                            """

                         tool.printMsg("Version No:${generateTag}")
                    }
               }

               failure {
                    script {
                         String status = '构建失败'
                         currentBuild.description = "\n ${status}!"
                         tool.printMsg("构建失败", 'red')
                         email.email(status, toEmail)
                    }
               }

               aborted {
                    script {
                         String status = '构建取消'
                         currentBuild.description = "\n ${status}!"
                         email.email(status, toEmail)
                    }
               }
          }
     }
}
