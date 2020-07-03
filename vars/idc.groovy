def call(Closure body) {
     body()

     def tool = new org.devOps.Tools()
     def email = new org.devOps.Email()
     def checkout = new org.devOps.Checkout()
     def build = new org.devOps.Build()
     def deploy = new org.devOps.Deploy()

     def targetIp = body.targetIp
     def toEmail = body.toEmail
     def runComposer = body.runComposer
     def phpSrc = body.phpSrc
     def repository = body.repository
     def jenkins2repositoryCredentialsId = body.jenkins2repositoryCredentialsId
     def jenkins2serverCredentialsId = body.jenkins2serverCredentialsId
     def www = body.www
     def domain = body.domain
     def tarName = "${domain}_${BUILD_ID}.tar.gz"


     tool.printMsg("Gavin' jenkinsfile share library", 'green')

     tool.printMsg("是否执行composer:${runComposer}", 'green')
     tool.printMsg("代码所在目录:${phpSrc}", 'green')
     tool.printMsg("params:${params}", 'green')
     tool.printMsg("代码仓库:${repository}", 'green')
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
            string(name: 'branch', defaultValue: 'develop', description: 'Please enter the code branch to be built')
            string(name: 'version', defaultValue: '', description: 'Please enter the version number to be published')
            choice(name: 'mode', choices: ['deploy', 'rollback'], description: '选择方向！')
        }

    	stages {
    	    stage ("Authorization") {
    	        if ('prod' == branch) {
                    steps {
                        timeout(time:5, unit:"MINUTES") {
                            input (
                                message: "Should we continue ?"
                                ok: "Yes, we should."
                                submitter: "gavin, admin" // 指定允许提交的用户
                                parameters {
                                    string(name: 'who', defaultValue: 'gavin', description: 'Who are you?')
                                }
                            )

                            if (submitter.contains("${who}") {
                                script {
                                    tool.printMsg("${who},同意发布", 'green')
                                }
                            } else {
                                tool.printMsg("${who},同意发布", 'red')
                                throw new RuntimeException("组长拒绝部署")
                                false
                            }
                        }
                    }
    	        }
    	    }
    		// 下载代码
    		stage("Checkout") { // 阶段名称
    			steps {
    				timeout(time:5, unit:"MINUTES") {  // 步骤超时时间
    					script {
    						tool.printMsg('开始:拉取代码', 'green')
    						checkout.checkout(repository, jenkins2repositoryCredentialsId, "${branch}")
    						tool.printMsg('结束:拉取代码', 'green')
    				 	}
    				}
    			}
    		}

    		// 构建
    		stage("Build") {
    			steps {
    				timeout(time:20, unit:"MINUTES") {
    					script {
    						tool.printMsg('开始:应用打包', 'green')
                            build.tar(domain, targetIp, jenkins2serverCredentialsId, tarName)
                            tool.printMsg('结束:应用打包', 'green')
    				 	}
    				}
    			}
    		}

            stage ("Deploy") {
                steps {
                    timeout(time:20, unit:"MINUTES") {
                        script {
                            tool.printMsg('开始:上传＆解压', 'green')
                            deploy.upload(domain, targetIp, jenkins2serverCredentialsId, phpSrc, runComposer, www, tarName)
                            tool.printMsg("结束:上传＆解压", 'green')
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
//     		always {
//     			script {
//     				println("always")
//     			}
//     		}

    		// currentBuild 全局变量，description 构建描述
    		success {
    			script {
    			    string status = '构建成功'
    				currentBuild.description = "\n ${status}!"
    				email.email(status, toEmail)
    			}
    		}

    		failure {
    			script {
    				def  status = '构建失败'
                    currentBuild.description = "\n ${status}!"
                    tool.printMsg("构建失败", 'red')
                    email.email(status, toEmail)
    			}
    		}

    		aborted {
    			script {
    				def  status = '构建取消'
                    currentBuild.description = "\n ${status}!"
                    email.email(status, toEmail)
    			}
    		}
    	}
    }
}