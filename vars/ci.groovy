def call(Closure body) {
     aa = body(bb, cc)

     def tool = new org.devOps.Tools()
     tool.printMsg("my lib", 'green')

     tool.printMsg("${bb}", 'green')
}