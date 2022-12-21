package chapter2

/**
 * 이런 유형의 클래스를 값 객체라고 부르며 다양한 언어가 값 객체를 간결하게 기술할 수 있는 구문을 제공한다.
 * 코틀린의 기본 접근 제한자는 public 이다.
 */
class PersonKotlin(val name: String) {
}

/**
 * 자바에서는 필드와 접근자를 한데 묶어 프로퍼티라고 부르며, 프로퍼티라는 개념을 활용하는 프레임워크가 많다.
 * 코틀린은 프로퍼티를 언어 기본 기능으로 제공하며, 코틀린 프로퍼티는 자바의 필드와 접근자 메서드를 완전히 대신한다.
 */
class PersonKotlin2 (
    val name: String,  // 읽기 전용 프로퍼티로, 비공개 필드와 필드를 읽는 단순한 공개 게터를 만들어 낸다.
    var isMarried: Boolean // 쓸 수 있는 프로퍼티로, 코틀린은 비공개 필드, 공개 게터, 공개 세터를 만들어 낸다.
)

/**
 * 커스텀 접근자
 */
class Rectangle(val height: Int, val width: Int) {
    /**
     * 이 프로퍼티에는 자체 구현을 제공하는 게터만 존재한다. 클라이언트가 프로퍼티에 접근할 때마다 게터가 프로퍼티 값을 매번 계산한다.
     */
    val isSquare: Boolean
        get() {                   // 프로퍼티 게터 선언
            return height == width
        }
}

/**
 * 코틀린에서의 클래스 사용법
 */
fun main(args: Array<String>) {
    val person = PersonKotlin2("Hong", true)
    println(person.name)
    println(person.isMarried)
//    person.name = "Kim" // Error Occured!
    person.isMarried = false
}

