// 回调demo
def call(Closure callableBody) {
    println('回调方法1111111');
    node('windows') {
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