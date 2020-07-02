def call(Closure body) {
     body()

     def tool = new org.devOps.Tools()
     def gitlab = new org.devOps.GitlabApi()

     gitlab.initVariable(body.gitlabServer, body.gitlabApiCredentialsId)
     println(body.gitlabApiCredentialsId)

     tool.printMsg("first line", 'green')

     tool.printMsg(body.gitlabServer, 'green')
     def gitlabServer = body.gitlabServer

     tool.printMsg(gitlabServer, 'green')
     tool.printMsg('获取工程id', 'green')
     tool.printMsg("工程名称${body.projectName}", 'green')

     def projectId = gitlab.getProjectID(body.gitlabApiCredentialsId, body.projectName)

     def res = gitlab.updateRepositoryFile(projectId, 'readme.md', "YWFhYWFhYWFh")

     println("${res}")

     body()
     println(body.phpSrc)
     println(body.debug)

     def checkout = new org.devOps.Checkout()
     def build = new org.devOps.Build()
     def deploy = new org.devOps.Deploy()
//      def gitlab = new org.devOps.GitlabApi()

     tool.printMsg("my lib", 'green')
     // tool.getProjectName(body.repository)
     tool.printMsg(body, 'green')

     tool.printMsg(body.runComposer, 'green')
     tool.printMsg(body.phpSrc, 'green')

     tool.printMsg("${params}", 'green')
     tool.printMsg('environment', 'green')
     tool.printMsg(body.repository, 'green')

    // jenkins 工作目录
    String workspace = "/home/soar/app/nginx-php-fpm/www/jenkins/workspace"

    pipeline {
    	agent {
    		node {
    			label "master" // 指定运行节点的标签或者名称
    			customWorkspace "projectName/${workspace}" // 指定运行工作目录（可选）
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
    		// 下载代码
    		stage("Checkout") { // 阶段名称
    			steps {
    				timeout(time:5, unit:"MINUTES") {  // 步骤超时时间
    					script { // 脚本式
    						println('fetch code')

    						//Git,拉取代码
    						checkout.checkout(body.repository, body.jenkins2repositoryCredentialsId, "${branch}")
    						println('get code ok')
    				 	}
    				}
    			}
    		}

    		// 构建
    		stage("Build") {
    			steps {
    				timeout(time:20, unit:"MINUTES") {
    					script { // 脚本式
    						println('Build tar')
                            build.tar('project-name', body.targetIp, body.jenkins2serverCredentialsId)
                            println('sshagent应用打包')
    				 	}
    				}
    			}
    		}

            stage ("Deploy") {
                steps {
                    timeout(time:20, unit:"MINUTES") {
                        script { // 脚本式
                            println('upload tar')
                            deploy.upload('project-name', body.targetIp, body.jenkins2serverCredentialsId, body.phpSrc, body.runComposer)
                            println('释放压缩文件')
                        }
                    }
                }
            }

    		// 代码扫描
    		stage("CodeScan") {
    			steps {
    				timeout(time:30, unit:"MINUTES") {
    					script { // 脚本式
    						println('代码扫描')
    						tool.printMsg("我的共享库", 'green')
    				 	}
    				}
    			}
    		}
    	}

    	post { // 构建后的操作
    		always {
    			script {
    				println("always")
    			}
    		}

    		// currentBuild 全局变量，description 构建描述
    		success {
    			script {
    				currentBuild.description += "\n 构建成功!"
    			}
    		}

    		failure {
    			script {
    				currentBuild.description += "\n 构建失败!"
    			}
    		}

    		aborted {
    			script {
    				currentBuild.description += "\n 构建取消!"
    			}
    		}
    	}
    }
}