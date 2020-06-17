// 回调demo
def call(Closure callableDemo) {
    callableDemo();
    println('回调方法');
    println(run_composer);
    println(php_project_path);
}