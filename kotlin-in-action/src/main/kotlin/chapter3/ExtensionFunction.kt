package chapter3

/**
 * 확장 함수
 * 코틀린은 기존 자바 프로젝트에 통합하는 경우에는 코틀린으로 직접 변환할 수 없거나 미처 변환하지 않은 기존 자바 코드를 처리할 수 있어야 한다.
 * 이런 기존 자바 API 를 재작성하지 않고도 코틀린이 제공하는 여러 편리한 기능을 사용할 수 있다면 정말 좋은 일이다.
 * 이런 역할을 확장함수가 해준다.
 * 확장 함수는 어떤 클래스의 멤버 메서드인 것처럼 호출할 수 있지만 그 캘래스의 밖에 선언된 함수다.
 *
 * 확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장할 클래스의 이름을 덧붙이기만 하면 된다. 클래스 이름을 수신 객체 타입이라 부르며,
 * 확장함수가 호출되는 대상이 되는 값을 수신 객체라고 부른다.
 */
fun String.lastChar(): Char = this.get(this.length - 1)

/**
 * 어떤 면에서 이는 String 클래스에 새로운 메서드를 추가하는 것과 같다.
 * JVM 언어로 작성된 클래스도 확장할 수 있다. 자바 클래스로 컴파일한 클래스 파일이 있는 한 그 클래스에 원하는 대로 확장을 추가할 수 있다.
 */

/**
 * 임포트와 확장함수
 * 확장 함수를 정의했다고 해도 자동으로 프로젝트 안의 모든 소스코드에서 그 함수를 사용할 수 있지는 않다. 확장 함수를 사용하기 위해서는 그 함수를 다른 클래스나
 * 함수와 마찬가지로 임포트해야만 한다. 확장 함수를 정의하자마자 어디서든 그 함수를 쓸 수 있다면 한 클래스에 같은 이름의 확장 함수가 둘 이상 있어서 이름이
 * 충돌하는 경우가 자주 생길 수 있다.
 * 한 파일 안에서 다른 여러 패키지에 속해있는 이름이 같은 함수를 가져와 사용해야 하는 경우 이름을 바꿔서 임포트하면 이름 충돌을 막을 수 있다.
 * import strings.lastCahr as last
 */

/**
 * 확장 함수는 오버라이드할 수 없다.
 * 확장 함수는 클래스의 일부가 아니다. 확장 함수는 클래스 밖에 선언된다. 이름과 파라미터가 완전히 같은 확장 함수를 기반 클래스와 하위 클래스에 대해 정의해도
 * 실제로는 확장 함수를 호출할 때 수신 객체로 지정한 변수의 정적 타입에 의해 어떤 확장 함수가 호출될지 결정되지, 그 변수에 저장된 객체의 동적인 타입에 의해 확장 함수가
 * 결정되지 않는다.
 */

// joinToString 의 최종 버전
fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for((index, element) in this.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun main(args: Array<String>) {
    println("Kotlin".lastChar())
    val list = listOf(1, 2, 3)
    println(list.joinToString(";", "(", ")"))
}