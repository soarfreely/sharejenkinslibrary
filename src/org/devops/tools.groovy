package org.devops

// 格式化输出
def printMsg(content,color) {
    colors = [
            'red'   : "\033[40;31m >>>>>>>>>>>${content}<<<<<<<<<<< \033[0m",
            'blue'  : "\033[47;34m ${content} \033[0m",
            'green' : "[1;32m>>>>>>>>>>${content}>>>>>>>>>>[m",
            'green1' : "\033[40;32m >>>>>>>>>>>${content}<<<<<<<<<<< \033[0m"
        ]

        ansiColor('xterm') {
        println(colors[color])
    }
}
