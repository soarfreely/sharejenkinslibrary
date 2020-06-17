// 回调demo
def call(name1, name2) {
    println('1111111');
}

def call2(Closure callableBody) {
    println('1111111');
//     println(run_composer);
//     println(php_project_path);
    println('2222222');
    node('callableDemo') {
        callableBody();
        println('回调方法1');
        println(run_composer);
        println(php_project_path);
    }
//     callableBody();
//     println('回调方法');
//     println(run_composer);
//     println(php_project_path);
}