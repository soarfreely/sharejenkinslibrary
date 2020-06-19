#!groovy
@Library('jenkinslib@master') _  // 加载共享库

def tool = new org.devOps.Tools()

ci {
   // 是否执行composer
   run_composer = 'true'

   // 源码目录
   php_project_path = 'trunk'

   // 仓库地址
   repository = 'ssh://git@github.com:soarfreely/lib-1.git'
}

