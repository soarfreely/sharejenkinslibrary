#!groovy
@Library('jenkinslib@master') _

ci {
   // 是否执行composer
   run_composer = 'true'

   // 源码目录
   php_project_path = 'trunk'

   // 仓库地址
   repository = 'https://github.com/soarfreely/lib-1.git'

   // jenkins2repository 凭据
   jenkins2repository = 'local-jenkins-github'

   // jenkins2server凭据
   jenkins2server = 'local-php'

   // 生产服务器ip
   targetIp = '172.17.0.3'
}
