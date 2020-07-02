#!groovy
@Library('jenkinslib@master') _

ci {
   // 是否执行composer
   runComposer = 'true'

   // 源码目录
   phpSrc = 'src'

   // jenkins share lib仓库地址
   repository = 'https://github.com/soarfreely/lib-1.git'
   // repository = 'http://172.17.0.3/group-a/lib-1.git'

   // jenkins2repository 凭据
   jenkins2repositoryCredentialsId = 'local-jenkins-github'

   // jenkins2server凭据
   jenkins2serverCredentialsId = 'local-php'

   // 生产服务器ip
   targetIp = '172.17.0.4'

   // 工程名称
   projectName = 'lib-1'

   // gitlab api 凭据
   gitlabApiCredentialsId = 'local-gitlab-api'

   // gitlab　api
   gitlabServer = 'http://172.17.0.3:80/api/v4'

   debug = 'debug'
}
