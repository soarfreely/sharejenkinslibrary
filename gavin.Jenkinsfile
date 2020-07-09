#!groovy
@Library('jenkinslib@master') _

idc {
   // 是否执行composer
   runComposer = 'true'

   www = '/root/www'

   domain = 'localhost'

   // 源码目录
   phpSrc = 'src'

   // 业务代码仓库地址
   repository = 'https://github.com/soarfreely/lib-1.git'
   // repository = 'http://172.17.0.3/group-a/lib-1.git'

   // jenkins2repository 凭据 (业务代码仓库)
   jenkins2repositoryCredentialsId = 'local-jenkins-github'

   // jenkins2server凭据 (生产服务器)
   jenkins2serverCredentialsId = 'local-php'

   // 生产服务器ip
   targetIp = '172.17.0.5'

   // toEmail
   toEmail = 'soarfreely.z@qq.com'

   // 工程名称
   projectName = 'lib-1'

   // gitlab api 凭据
   gitlabApiCredentialsId = 'local-gitlab-api'

   // gitlab　api
   gitlabServer = 'http://172.17.0.3:80/api/v4'

   debug = 'debug'
}
