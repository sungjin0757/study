package chapter7


// map 에서의 구조 분해
fun printEntries(map: Map<String, String>) {
    for((key, value) in map) {
        println("$key -> $value")
    }
}
fun main(args: Array<String>) {
    // 구조 분해
    val ppp = Point(10, 20)
    val (x, y) = ppp  // 내부적으로 componentN 함수 호출
}