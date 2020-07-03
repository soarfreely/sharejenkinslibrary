def call(Closure gavin) {
     gavin()

     def tool = new org.devOps.Tools()
     tool.printMsg('ci', 'green')
}