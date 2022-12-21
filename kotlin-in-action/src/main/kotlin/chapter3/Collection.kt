package chapter3

val set = hashSetOf(1, 7, 53)
val list = arrayListOf(1, 7, 53)
val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")


/**
 * 가변 길이 인자, 중위 함수 호출, 라이브러리 지원
 * vararg 키워드를 사용하면 호출 시 인자 개수가 달라질 수 있는 함수를 정의할 수 있다.
 * 중위함수 호출 구문을 사용하면 인자가 하나뿐인 메서드를 간편하게 호출할 수 있다.
 * 구조 분해 선언을 사용하면 복합적인 값을 분해해서 여러 변수에 나눠 담을 수 있다.
 */

/**
 * 자바 컬렉션 API 확장
 * 어떻게 자바 라이브러리 클래스의 인스턴스인 컬렉션에 대해 코틀린이 새로운 기능을 추가할 수 있을까?
 * 바로 확장 함수가 그 답이다.
 * 코틀린 표준 라이브러리는 수많은 확장 함수를 포함한다.
 */

/**
 * 가변 인자 함수
 * 자바의 가변 길이 인자에 익숙할 것이다. 가변 길이 인자는 메서드를 호출할 때 원하는 개수만큼 값을 인자로 넘기면 자바 컴파일러가 배열에 그 값들을 넣어주는
 * 기능이다.
 * 코틀린의 가변 길이 인자도 자바 와 비슷하다.
 * 다만 문법이 조금 다르다. 타입 뒤에 ... 를 붙이는 대신 코틀린에서는 파라미터 앞에 vararg 변경자를 붙인다.
 * 이미 배열에 들어있는 원소를 가변 길이 인자로 넘길 때도 코틀린과 자부 구문이 다르다.
 * 자바에서는 배열을 그냥 넘기면 되지만 코틀린에서는 배열을 명시적으로 풀어서 배열의 각 원소가 인자로 전달되게 해야한다.
 * 기술적으로는 스프레드 연산자가 그런 작업을 해준다. 하지만 실제로는 전달하려는 배열 앞에 * 를 붙이기만 하면 된다.
 */

fun varArgTest(vararg num: Int) {
    for(i in 1..num.size) {
        print("${num[i - 1]} ")
    }
}

/**
 * 값의 쌍 다루기
 * to 는 코틀린 키워드가 아니다. 이 코드는 중위 호출이라는 특별한 방식으로 to 라는 일반 메서드를 호출한 것이다.
 * 중위 호출 시에는 수신 객체와 유일한 메서드 인자 사이에 메서드 이름을 넣는다.
 * 1.to("one") 일반 방식
 * 1 to "one" 중위 호출 방식
 * 인자가 하나뿐인 일반 메서드나 인자가 하나뿐인 확장 함수에 중위 호출을 사용할 수 있다.
 * 함수를 중위 호출에 사용하게 허용하고 싶으면 infix 변경자를 함수 선언 앞에 추가해야 한다.
 */
infix fun Any.to(other: Any) = Pair(this, other)

fun main(args: Array<String>) {
    /**
     * 코틀린이 자체 컬렉션을 사용하지 않는 이유
     * 자바코드와 상호작용하기 쉽기 위하여
     * 자바와 코틀린 컬렉션을 서로 반환할 필요가 없어짐
     */
    println(set.javaClass) // javaClass 는 getClass 를 의미함. 즉, 코틀린 자신만의 컬렉션을 제공하지 않는 다는 뜻.
    println(list.javaClass)
    println(map.javaClass)

    // 자바 컬렉션 API 확장의 예
    val strings: List<String> = arrayListOf("first", "second", "fourteenth")
    println(strings.last()) // fourteenth

    val numbers: List<Int> = arrayListOf(1, 123123123, 2)
    println(numbers.max())

    // 가변인자
    varArgTest(1, 2, 3)
    println()
    val list: List<String> = arrayListOf("args: ", *args)
    println(list)

    // 구조 분해 선언
    val (number, name) = 1 to "one"
}