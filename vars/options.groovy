def call(Closure body) {
    node('options') {
       body()

        // 指定运行选项（可选）
        timestamps() // 日志会有日志
        skipDefaultCheckout() // 删除隐藏checkout scm 语句
        disableConcurrentBuilds() // 禁止并行（根据实际情况）
        timeout(time:1, unit:"HOURS") // 流水线超时设置
        // 表示保留6次构建历史
        buildDiscarder(logRotator(daysToKeepStr:'1', numToKeepStr:'6', artifactDaysToKeepStr:'2', artifactNumToKeepStr:'5'))
    }
}