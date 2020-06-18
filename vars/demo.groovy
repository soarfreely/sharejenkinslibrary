def call(Closure body) {
    node('demo') {
       body()
    }
}
