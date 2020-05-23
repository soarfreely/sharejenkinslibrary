#!groovy
@Library('jenkinslib@master') _  // 加载共享库

def tool = new org.devops.tools()

abc()

String workspace = "/opt/jenkins/workspace"

pipeline {
	
	agent {
		node {
			label "master" // 指定运行节点的标签或者名称
			customWorkspace "${workspace}" // 指定运行工作目录（可选）
		}
	}

	options {  // 指定运行选项（可选）
		timestamps() // 日志会有日志
		skipDefaultCheckout() // 删除隐藏checkout scm 语句
		disableConcurrentBuilds() // 禁止并行（根据实际情况）
		timeout(time:1, unit:"HOURS") // 流水线超时设置
	}


	stages {
		// 下载代码
		stage("GetCode") { // 阶段名称
			steps {
				timeout(time:5, unit:"MINUTES") {  // 步骤超时时间
					script { // 脚本式
						println('获取代码')
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
						tool.printMsg("我的共享库")
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
