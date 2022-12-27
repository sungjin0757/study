package chapter6

import java.io.BufferedReader

/**
 * 컬렉션과 배열
 */

/**
 * null 가능성과 컬렉션
 * 컬렉션 안에 null 값을 넣을 수 있는지 여부는 어떤 변수의 값이 null 이 될 수 있는지 여부와 마찬가지로 중요하다. 변수 타입 뒤에 ? 를 붙이면
 * 그 변수에 null 을 저장할 수 있다는 뜻인 것 처럼 타입 인자로 쓰인 타입에도 같은 표시를 사용할 수 있다.
 */
fun readNumbers(reader: BufferedReader): List<Int?> {
    val result = ArrayList<Int?>()
    for (line in reader.lineSequence()) {
        try {
            val number = line.toInt()
            result.add(number)
        } catch (e: NumberFormatException) {
            result.add(null)
        }
    }
    return result
}

/**
 * 어떤 변수 타입의 null 가능성과 타입 파라미터로 쓰이는 타입의 null 가능성 사이의 차이를 살펴보자.
 * List<Int?>
 * List<Int>? 리스트 자체가 null 이 될 수 있다.
 */

fun addValidNumbersV1(numbers: List<Int?>) {
    var sumOfValidNumbers = 0
    var invalidNumbers = 0
    for (number in numbers) {
        if (number != null) {
            sumOfValidNumbers += number
        } else {
            invalidNumbers++
        }
    }
    println("Sum of valid numbers : $sumOfValidNumbers")
    println("Sum of invalid numbers : $invalidNumbers")
}

fun addValidNumbersV2(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()
    println("Sum of valid numbers : ${validNumbers.sum()}")
    println("Sum of invalid numbers : ${numbers.size - validNumbers.size}")
}

/**
 * 읽기 전용과 변경 가능한 컬렉션
 * 코틀린 컬렉션과 자바 컬렉션을 나누는 가장 중요한 특성 하나는 코틀린에서는 컬렉션안의 데이터에 접근하는 인터페이스와 컬렉션 안의 데이터를 변경하는
 * 인터페이스를 분리했다는 점이다.
 * 코틀린 컬렉션을 다룰 때 사용하는 가장 기초적인 인터페이스인 kotlin.collections.Collection 부터 시작한다.
 * 컬렉션의 데이터를 수정하려면 kotlin.collections.MutableCollection 인터페이스를 사용한다.
 * 코드에서 가능하면 항상 읽기 전용 인터페이스를 사용하는 것을 일반적인 규칙으로 삼아야한다.
 * 어떤 함수가 MutableCollection 이 아닌 Collection 타입의 인자를 받는 다면 그 함수는 컬렉션을 변경하지 않고 읽기만 한다.
 * 반면 어떤 함수가 MutableCollection 을 인자로 받는다면 그 함수가 컬렉션의 데이터를 바꾸리라 가정할 수 있다.
 *
 * 읽기 전용 컬렉션이라고 해서 꼭 변경 불가능한 컬렉션일 필요는 없다. 읽기 전용 인터페이스 타입인 변수를 사용할 때
 * 그 인터페이스는 실제로는 어떤 컬렉션 인스턴스를 가리키는 수많은 참조 중 하나일 수 있다.
 * 즉, 참조 중에 변경 가능한 컬렉션이 있을 수도 있다. 그러니 읽기전용이라고 해서 항상 스레드 안전 상태는 아니란 말이다.
 */
fun <T> copyElements(source: Collection<T>, target: MutableCollection<T>) {
    for (item in source) {
        target.add(item)
    }
}

/**
 * 코틀린 컬렉션과 자바
 * 모든 코틀린 컬렉션은 그에 상응하는 자바 컬렉션 인터페이스의 인스턴스라는 점은 사실이다.
 * 코틀린의 읽기 전용과 변경 가능 인터페이스의 기본 구조는 java.util 패키지에 있는 자바 컬렉션 인터페이스의 구조를 그대로 옮겨 놓았자.
 * 추가로 변경 가능한 각 인터페이스는 자신과 대응하는 읽기 전용 인터페이스를 확장한다.
 * 변경 가능한 인터페이스는 java.util 패키지에 있는 인터페이스와 직접적으로 연관되지만 읽기 전용 인터페이스에는 컬렉션을 변경할 수 있는 모든 요소가 빠져있다.
 *
 * 자바 표준 클래스를 코틀린에서 어떻게 취급하는지 보여주기 위해 java.util.ArrayList 와 java.util.HashSet 클래스가 들어있다.
 * 코틀린은 이들이 마치 각각 코틀린의 MutableList 와 MutableSet 인터페이스를 상속한 것처럼 취급한다.
 *
 * 자바 메서드를 호출하되 컬렉션을 인자로 넘겨야 한다면 따로 변환하거나 복사하는 등의 추가 작업 없이 직접 컬렉션을 넘기면 된다.
 * 이 경우에 자바는 읽기 전용 컬렉션과 변경 가능 컬렉션을 구분하지 않으므로, 코틀린에서 읽기전용 Collection 으로 선언된 객체라도 자바 코드에서는
 * 그 컬렉션 객체의 내용을 변경할 수 있다.
 */

/**
 * 객체의 배열과 원시 타입의 배열
 * 코틀린 배열은 타입 파라미터를 받는 클래스다. 배열의 원소 타입은 바로 그 타입 파라미터에 의해 정해진다.
 * 코틀린에서 배열을 만드는 방법은 다음과 같다
 * arrayOf 함수에 원소를 넘기면 배열을 만들 수 있다.
 * arrayOfNulls 함수에 정수 값을 인자로 넘기면 모든 원소가 null 이고 인자로 넘긴 값과 크기가 같은 배열을 만들 수 있다. 물론 원소타입이 null 이 될 수 있어야한다.
 * Array 생성자는 배열 크기와 람다를 인자로 받아서 람다를 호출해서 각 배열 원소를 초기화해준다. arrayOf를 쓰지 않고 각 원소가 null 이 아닌 배열을 만들어야
 * 하는 경우 사용한다.
 * Array 를 사용하며 배열을 만들었을 경우 타입은 Integer 로 래퍼 타입이다.
 * 원시 타입으로 배열을 표현하기 위해서는 IntArray, ByteArray ... 를 사용하면된다.
 *
 * 원시 타입의 배열을 만드는 방법은 다음과 같다.
 * 각 배열 타입의 생성자는 size 인자를 받아서 해당 원시 타입의 디폴트 값으로 초기화된 size 크기의 배열을 반환한다.
 * 팩토리 함수는 여러 값을 가변 인자로 받아서 그런 값이 들어간 배열을 반환한다. intArrayOf..
 * 크기와 람다를 인자로 받는 생성자를 사용한다.
 */

fun main(args: Array<String>) {
    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
//    copyElements(target, source) // error occurred 실제 그 값이 변경 가능한 컬렉션인이 여부와 관계없이 선언된 타입이 읽기 전용이라면 target 에 넘기면 오류가 난다.

    // 배열 만들기
    val letters = Array<String>(26){i -> ('a' + i).toString()}
    val numbers = Array<Int>(4){ i -> 0}

    for ((index, vale) in numbers.withIndex()) {
        println("$index, $vale")
    }

    val sourceList = source.toTypedArray() // 손쉽게 컬렉션을 배열로 만들 수 있다.

    val fiveZeros = IntArray(5)
    val fiveZerosToo = intArrayOf(0, 0, 0, 0, 0)
    val squares = IntArray(5) {i -> (i + 1) * (i + 1)}
}