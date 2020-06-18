def call(Closure body) {
     aa = body()

     def tool = new org.devOps.Tools()
     tool.printMsg("my lib", 'green')

     tool.printMsg("${body}", 'green')
}