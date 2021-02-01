#!groovy
@Library('jenkinslib@cicd') _

cd {
   // 是否执行composer
   runComposer = 'true'

   www = '/home/soar/CICD/www'

   domain = 'local.7fw'

   // 源码目录
   phpSrc = 'src'

   // 仓库名称
   repositoryName = '7fw'

   // nginx代理端口
   nginxProxyPort = 10081

   // 业务代码仓库地址
   repositoryPath = 'git@github.com:soarfreely/72fw.git'

   // Harbor api　auth
   imageRepositoryAuth = 'YWRtaW46YWxpMjI5LUhhcmJvcg=='

   // 镜像仓库
   imageRepoUri = '39.100.108.229/library'

   // jenkins2repository 凭据 (业务代码仓库)
   jenkins2repositoryCredentialsId = 'local-jenkins-github'

   // jenkins2server凭据 (生产服务器)
   jenkins2serverCredentialsId = 'deepin-2-39'

   // 生产服务器ip
   targetIp = '39.100.108.229'

   // toEmail
   toEmail = '346777749@qq.com'

   // 工程名称
   projectName = 'Gavin-project'

   // gitlab api 凭据
   gitlabApiCredentialsId = 'local-gitlab-api'

   // gitlab　api
   gitlabServer = 'http://172.17.0.3:80/api/v4'

   debug = 'debug'
}