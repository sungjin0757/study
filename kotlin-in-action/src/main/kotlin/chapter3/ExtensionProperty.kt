package chapter3

/**
 * 확장 프로퍼티
 * 확장 프로퍼티를 사용하면 기존 클래스 객체에 대한 프로퍼티 형식의 구문으로 사용할 수 있는 API 를 추가할 수 있다.
 * 프로퍼티라는 이름으로 불리기는 하지만 상태를 저장할 적절한 방법이 없기 때문에 (기존 클래스의 인스턴스 객체에 필드를 추가할 방법은 없다) 실제로 확장 프로퍼티는
 * 아무 상태도 가질 수 없다.
 */

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length -1 , value)
    }

fun main(args: Array<String>) {
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
}