#!groovy
@Library('jenkinslib@master') _  // 加载共享库

def tool = new org.devops.tools()

gavin()

String workspace = "/opt/jenkins/workspace"

pipeline {

	agent {
		node {
			label "master" // 指定运行节点的标签或者名称
			customWorkspace "${workspace}" // 指定运行工作目录（可选）
		}
	}

    //全局变量
    environment {
        activeEnv = 'project/123'
    }

	options {  // 指定运行选项（可选）
		timestamps() // 日志会有日志
		skipDefaultCheckout() // 删除隐藏checkout scm 语句
		disableConcurrentBuilds() // 禁止并行（根据实际情况）
		timeout(time:1, unit:"HOURS") // 流水线超时设置
		// 表示保留6次构建历史
		buildDiscarder(logRotator(daysToKeepStr:'1', numToKeepStr:'6', artifactDaysToKeepStr:'2', artifactNumToKeepStr:'5'))
	}

    // 参数
    parameters {
            string(name: 'branchName', defaultValue: 'master', description: '请输入将要构建的代码分支')
            choice(name: 'mode', choices: ['deploy', 'rollback'], description: '选择方向！')
    }

	stages {
		// 下载代码
		stage("GetCode") { // 阶段名称

		    // 局部变量
		    environment {
		        // 凭证id
                 credentialsId = 'jenkins'

                 // 仓库地址
                 srcUrl = "git@172.17.0.3:group-a/project-demo.git"
            }

			steps {
				timeout(time:5, unit:"MINUTES") {  // 步骤超时时间
					script { // 脚本式
						println('获取代码')

                        // 获取ssh 认证的凭证信息。(不同的凭证获取的方式不同)
                        withCredentials([sshUserPrivateKey(credentialsId: "${credentialsId}", keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'userName')]) {
                            println(identity)
                            println(userName)
                        }


                        wrap([$class: 'BuildUser']){
                            echo "full name is $BUILD_USER"
                            echo "user id is $BUILD_USER_ID"
                            echo "user email is $BUILD_USER_EMAIL"

                            // 当前构建用户，赋值给 构建描述
                            currentBuild.description = "$BUILD_USER"
                        }


						//Git,拉取代码
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: "${branchName}"]],
                            doGenerateSubmoduleConfigurations: false,
                            extensions: [],
                            submoduleCfg: [],
                            userRemoteConfigs: [
                                [
                                    credentialsId: "${credentialsId}",
                                    url: "${srcUrl}"
                                ]
                            ]
                        ])
				 	}
				}
			}
		}

		// 构建
		stage("Build") {
			steps {
				timeout(time:20, unit:"MINUTES") {
					script { // 脚本式
						println('应用打包')
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
