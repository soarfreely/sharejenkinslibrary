// 回调demo
// def call(name1, name2) {
//     println('1111111');
// }

def call(Closure callableBody) {
    println('1111111');
    callableBody.call();

    println('2222222');

    println(callableBody);
//     callableBody();
    println('33333333333');
    println(run_composer);
    println(php_project_path);

//     callableBody();
//     println('回调方法');
//     println(run_composer);
//     println(php_project_path);
}