#!groovy
@Library('jenkinslib@cicd') _

ci {
   // 是否执行composer
   runComposer = 'true'

   www = '/home/soar/CICD/www'

   domain = 'local.7fw'

   // 源码目录
   phpSrc = 'src'

   // 仓库名称
   repositoryName = '7fw'

   // 业务代码仓库地址
   repositoryPath = 'git@github.com:soarfreely/72fw.git'
   // repository = 'https://github.com/soarfreely/lib-1.git'
   // repository = 'http://172.17.0.3/group-a/lib-1.git'

   // jenkins2repository 凭据 (业务代码仓库)
   jenkins2repositoryCredentialsId = 'local-jenkins-github'

   // jenkins2server凭据 (生产服务器)
   jenkins2serverCredentialsId = 'deepin-2-39'

   // 镜像仓库
   imageRepoUri = '39.100.108.229/library'

   // 生产服务器ip
   targetIp = '39.100.108.229'
//   targetIp = '172.17.0.5'

   // toEmail
   toEmail = 'soarfreely.z@gmail.com'

   // 工程名称
   projectName = 'Gavin-project'

   // gitlab api 凭据
   gitlabApiCredentialsId = 'local-gitlab-api'

   // gitlab　api
   gitlabServer = 'http://172.17.0.3:80/api/v4'

   debug = 'debug'
}