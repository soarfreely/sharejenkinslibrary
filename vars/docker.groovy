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
    def repository = body.repository
    def jenkins2repositoryCredentialsId = body.jenkins2repositoryCredentialsId
    def jenkins2serverCredentialsId = body.jenkins2serverCredentialsId
    def www = body.www
    def repo = body.repo
    def domain = body.domain
    def tarName = "${domain}_${BUILD_ID}.tar.gz"
    def submitter = "gavin, admin"


    tool.println("domain:${domain}")
    def generateTag = tool.generateTag(domain)


    tool.printMsg("Gavin' jenkinsfile share library", 'green')

    tool.printMsg("是否执行composer:${runComposer}", 'green')
    tool.printMsg("代码所在目录:${phpSrc}", 'green')
    tool.printMsg("params:${params}", 'green')
    tool.printMsg("代码仓库:${repository}", 'green')
    tool.printMsg("jenkins2repository凭据:${jenkins2repositoryCredentialsId}", 'green')
    tool.printMsg("jenkins2server凭据:${jenkins2serverCredentialsId}", 'green')
    tool.printMsg("目标服务器:${targetIp}", 'green')
    tool.printMsg("负责人邮箱:${toEmail}", 'green')

    // Harbor仓库镜像详情接口
    String basicAuth = "Basic " + ("admin:ali229-Harbor".bytes.encodeBase64().toString())
    def imageResponse = harbor.imageDetail("http://39.100.108.229/api/repositories/library/${domain}/tags/${branchOrTag}", basicAuth)
    String imageDigest = (boolean)imageResponse.get('digest', null)
    tool.printMsg("imageResponse,1:${imageDigest}", 'green')

    // Github分支详情接口
    boolean branchName = false
    if (!imageDigest) {
        // 当前branchOrTag不是镜像tag
        tool.printMsg("开始:拉取代码,1:${branchOrTag}", 'green')
        def branchResponse = github.branchDetail(repo, branchOrTag)
        tool.printMsg("开始:拉取代码,2:${branchResponse.get('name', null)}", 'green')
        branchName = (boolean)branchResponse.get('name', null)
        tool.printMsg("开始:拉取代码,3:${branchName}", 'green')
    } else {
        tool.printMsg("debug:Harbor仓库镜像存在", 'green')
    }

    // jenkins 工作目录
    pipeline {
        agent any
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
            string(name: 'branchOrTag', defaultValue: 'develop', description: 'Please enter the code branch or tag to be built')
            choice(name: 'mode', choices: ['deploy', 'rollback'], description: '选择方向！')
        }

        stages {
            // 下载代码
            stage("Checkout") {
                steps {
                    timeout(time:5, unit:"MINUTES") {
                        script {
                            if (!imageDigest) {
                                if (!branchName) {
                                    throw new Exception("输入的branchOrTag:${branchOrTag}错误")
                                } else {
                                    tool.printMsg("开始:拉取代码,Tag:${branchOrTag}", 'green')
                                    checkout.checkoutCode(repository, jenkins2repositoryCredentialsId, branchOrTag)
                                    tool.printMsg("结束:拉取代码,Tag:${branchOrTag}", 'green')
                                }
                            }
                        }
                    }
                }
            }

            // 构建
            stage("Build") {
                steps {
                    timeout(time:20, unit:"MINUTES") {
                        script {
                            if (!imageDigest) {
                                if (!branchName) {
                                    throw new Exception("输入的branchOrTag:${branchOrTag}错误")
                                } else {
                                    tool.printMsg('开始:拉取基础镜像', 'green')
                                    branchOrTag = generateTag
                                    build.build(domain, branchOrTag)
                                    tool.printMsg("debug:${branchOrTag}", 'green')
                                    tool.printMsg('结束:拉取基础镜像', 'green')
                                }
                            }
                        }
                    }
                }
            }

            stage ("Deploy") {
                steps {
                    timeout(time:20, unit:"MINUTES") {
                        script {
                            tool.printMsg('开始:拉取业务镜像&部署', 'green')
                            tool.printMsg("Deploy-debug:${branchOrTag}", 'green')
                            deploy.deploy(domain, branchOrTag)
////                            deploy.deploy(domain, targetIp, jenkins2serverCredentialsId, phpSrc, runComposer, www, tarName)
                            tool.printMsg("结束:拉取业务镜像&部署", 'green')
                        }
                    }
                }
            }

            // 代码扫描
            stage("CodeScan") {
                steps {
                    timeout(time:30, unit:"MINUTES") {
                        script { // 脚本式
                            tool.printMsg("代码扫描", 'green')
                        }
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

// jenkins-拉取代码
// jenkins-dockerfile 拷贝代码到镜像
// jenkins-push镜像到仓库
// jenkins-删除镜像

//部署服务器-拉取镜像&构建容器

// docker run -v 代码所在目录:容器html目录






//
//在做Jenkins pipeline groovy脚本时，遇到如下脚本
//
//def branches=[:]
//for(int i=0;i<10;++i){
//    branches[i]={
//        println i
//    }
//}
//prarallel branches
//
//结果发现打印出来所有branch里的i都是10。代表循环变量不是运行时传入branches块。做如下修改
//
//def branches=[:]
//for(int i=0;i<10;++i){
//    def value=i
//    def branch={
//        println value
//    }
//    branches.add(i,branch)
//}
//prarallel branches
//于是，打印结果时从0-9，符合期望。


//retry(3) {
//    for (int i = 0; i < 10; i++) {
//        branches["branch${i}"] = {
//            node {
//                retry(3) {
//                    checkout scm
//                }
//                sh 'make world'
//            }
//        }
//    }
//    parallel branches
//}