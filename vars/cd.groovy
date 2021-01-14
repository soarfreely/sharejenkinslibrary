import org.devops.Tools

static def call(Closure gavin) {
     gavin()

     def tool = new Tools()
     tool.printMsg('cd', 'green')
     tool.printMsg('cdkkk', 'green')
}

