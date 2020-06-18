def call(Closure body) {
     body()

     def tool = new org.devOps.Tools()
     tool.printMsg("my lib", 'green')

     tool.printMsg(body.run_composer, 'green')
     tool.printMsg(body.php_project_path, 'green')
}