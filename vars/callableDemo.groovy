// 回调demo
def call(Closure callableBody) {
    callableBody();
    println('回调方法');
    println(run_composer);
    println(php_project_path);
}