package org.devops

// 格式化输出
def printMsg(content, color = "green") {
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

// 通过仓库地址截取项目名称
def getProjectName(repository) {
    def proName = 'default'

    int pos = repository.indexOf('/')
    if (pos != -1) {
        projectName(repository.subString(pos + 1))
    } else {
        proName = repository.subString(pos + 1, indexOf('.git'))
    }

    println(proName)
}

// 获取文件内容
def readFileContent(filePath) {
    File file = new File(filePath)
    // 以字符串方式全部读取
//     println(file.text)
    println('readFileContent:')
    println(file.bytes.encodeBase64().toString())
    def base64content = file.bytes.encodeBase64().toString()

    return base64content
}

encodeBase64File(path) {
		File file = new File(path);
		FileInputStream inputFile;
		byte[] buffer = null;

		try {
			inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Base64.getEncoder().encodeToString(buffer);
}

/**
 * 获取随机的tag
 *
 * @return
 */
static def generateTag(domain = "localhost") {
    return "${domain}_" + (new Date().format('YYYYMMDDHHmmss')) + ((Math.random() * 10000) .toInteger()).toString()
}
