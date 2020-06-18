def call(Closure body) {
     paramsMap = body
     body()

     def tool = new org.devOps.Tools()
     def getCode = new org.devOps.GetCode()

     tool.printMsg("my lib", 'green')

     tool.printMsg(paramsMap, 'green')

     tool.printMsg(body.run_composer, 'green')
     tool.printMsg(body.php_project_path, 'green')

     //全局变量
     environment {
         runComposer = body.run_composer
         projectPath = body.php_project_path
         repository = body.repository
     }

     tool.printMsg("${params}", 'green')
     tool.printMsg('environment', 'green')
     tool.printMsg(body.repository, 'green')

    // jenkins 工作目录
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
    		// 表示保留6次构建历史
    		buildDiscarder(logRotator(daysToKeepStr:'1', numToKeepStr:'6', artifactDaysToKeepStr:'2', artifactNumToKeepStr:'5'))
    	}

        // 参数
        parameters {
            string(name: 'branchName', defaultValue: 'master', description: 'Please enter the code branch to be built')
            string(name: 'versionNo', defaultValue: '', description: 'Please enter the version number to be published')
            choice(name: 'mode', choices: ['deploy', 'rollback'], description: '选择方向！')
        }

    	stages {
    		// 下载代码
    		stage("GetCode") { // 阶段名称
    		    // 局部变量
    		    environment {
    		        // 凭证id
                     credentialsId = 'jenkins'
                }

    			steps {
    				timeout(time:5, unit:"MINUTES") {  // 步骤超时时间
    					script { // 脚本式
    						println('fetch code')

    						//Git,拉取代码
    						getCode.GetCode(body.repository, credentialsId, "${branchName}")
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
    						sshagent(['jenkins--ssh-deepin']) {
                               sh('ls -al')
                               sh("tar zvf project.tar ./* --exclude=./git")
                               sh '''
                                ssh -o StrictHostKeyChecking=no -l root 172.17.0.5 uname -a
                                echo 123
                                scp project.tar 172.17.0.5:/home/www
                                echo 456
                                ssh root@172.17.0.5 -tt "ls -al && cd /home/www && tar zxvf project.tar -C /home/www"
                               '''

                            }
                            println('sshagent应用打包')
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