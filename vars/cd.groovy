def call(Closure gavin) {
     gavin()

     def tool = new org.devOps.Tools()
     tool.printMsg('cd', 'green')
}