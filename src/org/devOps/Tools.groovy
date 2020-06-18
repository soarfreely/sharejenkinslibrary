package org.devOps

// 格式化输出
def printMsg(content, color) {
    colors = [
            'red'   : "\033[40;31m >>>>>>>>>>>Gavin Tips:${content}<<<<<<<<<<< \033[0m",
            'blue'  : "\033[47;34m Gavin Tips:${content} \033[0m",
            'green' : "[1;32m>>>>>>>>>>Gavin Tips:${content}>>>>>>>>>>[m",
            'vert' : "\033[40;32m >>>>>>>>>>>Gavin Tips:${content}<<<<<<<<<<< \033[0m"
        ]

        ansiColor('xterm') {
        println(colors[color])
    }
}

//获取源码目录
def buildDir(workspace, moduleName) {
    def srcDir = workspace

    buildDir = "${workspace}"
    if(moduleName == "null"){
        srcDir = "${workspace}"
    }else{
        srcDir = "${workspace}/${moduleName}"
    }

    return srcDir
}
