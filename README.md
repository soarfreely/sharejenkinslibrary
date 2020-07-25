# share jenkins library
>
##　简介
    使用共享库，将“可变参数”与发布逻辑剥离，便于快速部署，及后期维护。

##　使用
1. 配置
    jenkins => 系统配置 => Global Pipeline Libraries
2. jenkinsfile
    业务中创建Jenkinsfile
3. 发布任务 => 配置　=> 流水线 => Pipeline script from SCM

