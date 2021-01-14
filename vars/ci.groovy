import org.devops.Tools

def call(Closure gavin) {
     gavin()

     def tool = new Tools()
     tool.printMsg('ci', 'green')
     tool.printMsg("当前用户:${BUILD_USER}", 'green')
}