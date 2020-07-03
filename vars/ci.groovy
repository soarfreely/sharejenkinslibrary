def call(Closure gavin) {
     gavin()

     def tool = new org.devOps.Tools()
     tool.printMsg('ci', 'green')
     tool.printMsg("当前用户:${BUILD_USER}", 'green')
}