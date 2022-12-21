package chapter2

import java.util.TreeMap

/**
 * 이터레이션
 * for 루프
 * 코틀린에는 자바의 for 루프에 해당하는 요소가 없다. 이런 루프의 가장 흔한 용례인 초깃값, 증가 값, 최종 값을 사용한 루프를
 * 대신하기 위해 코틀린에서는 범위를 사용한다.
 * 범위는 기본적으로 두 값으로 이뤄진 구간이다. 보통은 그 두 값은 정수 등의 숫자 타입의 값이며, .. 연산자로 시작 값과 끝값을 연결해서 범위를 만든다.
 * 코틀린의 범위는 폐구간 또는 양 끝을 포함하는 구간이다.
 */
val oneToTen = 1..10

fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz "
    i % 3 == 0 -> "Fizz "
    i % 5 == 0 -> "Buzz "
    else -> "$i "
}

/**
 * in 으로 컬렉션이나 범위의 원소 검사
 * in 연산자를 사용해 어떤 값이 범위에 속하는지 검사할 수있다. 반대로 !in 을 사용하면 어떤 값이 범위에 속하지 않는지 검사할 수 있다.
 */
fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isDigit(c: Char) = c !in '0'..'9'
fun recognize(c: Char) = when(c) {
    in '0'..'9' -> "It's a digit"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter"
    else -> "I don't know"
}

/**
 * 범위는 문자에만 국한되지 않는다. 비교가 가능한 클래스라면 그 클래스의 인스턴스 객체를 사용해 범위를 만들 수 있다.
 * Comparable 을 사용하는 범위의 경우 그 범위 내의 모든 객체를 항상 이터레이션 하지는 못한다. 예를 들어 'java' 와 'kotlin' 사이의 모든 문자열을
 * 이터레이션 할 수 있을까? 그럴 수 없다.
 * 컬렉션에도 마찬가지로 in 을 사용할 수 있다.
 */
fun main(args: Array<String>) {
    for(i in 1..100) {
        print(fizzBuzz(i))
    }

    println()

    // downTo 는 역방향 수열을 만든다
    // step 은 증감값을 나타낸다.
    for(i in 100 downTo 1 step 2) {
        print(fizzBuzz(i));
    }

    // 끝 값을 포함시키고 싶지 않을 때는 until을 사용하면 된다.
    for(i in 1 until 100 step 1) {
        println(i)
    }

    // 맵에 대한 이터레이션
    val binaryReps = TreeMap<Char, String>()

    // .. 연산자를 숫자 뿐만 아니라 문자에도 활용할 수 있다
    for(c in 'A' .. 'F') {
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary // get 과 put 을 사용하는 대신 인덱싱 처럼 사용할 수 있다.
    }

    for((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    val list = arrayListOf("10", "11", "1001")
    for((index, element) in list.withIndex()) {
        println("${index}: $element")
    }
}
