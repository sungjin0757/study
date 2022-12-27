package chapter6

import java.lang.IllegalStateException

/**
 * 코틀린의 원시 타입
 * Int, Boolean, Any 등의 원시 타입에 대해 살펴 본다.
 * 코틀린은 원시 타입과 래퍼 타입을 구분하지 않는다. 또한 Object, Void 등의 자바 타입과 코틀린 타입 간의 대응 관계에 대해서도 살펴본다.
 */

/**
 * 원시타입 : Int, Boolean 등
 * 자바는 원시타입과 참조 타입을 구분한다.
 * 코틀린은 원시 타입과 래퍼타입을 구분하지 않으므로 항상 같은 타입을 사용한다.
 */
val i: Int = 1
val list: List<Int> = listOf(1, 2, 3)

// 코틀린에서는 숫자 타입 등 원시 타입의 값에 대해 메서드를 호출할 수 있다.
fun showProgress(progress: Int) {
    val percent = progress.coerceIn(0, 100)
    println("We're ${percent} done!")
}

/**
 * 실행 시점에 숫자 타입은 가능한 한 가장 효율적인 방식으로 표현된다. 대부분의 경우 코틀린의 Int 타입은 자바의 int 타입으로 컴파일 된다.
 * 이런 컴파일이 불가능한 경우는 컬렉션과 같은 제네릭 클래스를 사용하는 경우뿐 이다.
 * 예를 들어 Int 타입을 컬렉션의 타입 파라미터로 넘기면 그 컬렉션에는 Int의 래퍼 타입에 해당하는 java.lang.Integer 객체가 들어간다.
 */

/**
 * null 이 될 수 있는 원시 타입 : Int?, Boolean? 등
 * null 참조를 자바의 참조 타입의 변수에만 대입할 수 있기 때문에 null이 될 수 있는 코틀린 타입은 자바 원시타입으로 표현할 수 있다.
 */
data class Person3(
    val name: String,
    val age: Int? = null
) {
    fun isOlderThan(other: Person3): Boolean? {
        if(age == null || other.age == null)
            return null
        return age > other.age
    }
}

/**
 * 숫자 변환
 * 코틀린과 자바의 가장 큰 차이점 중 하나는 숫자를 변환하는 방식이다. 코틀린은 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않는다.
 * 결과 타입이 허용하는 숫자의 범위가 원래 타입의 범위보다 넓은 경우 조차도 변환은 불가능하다.
 */
val num = 1
//val numLong: Long = num // Error
val numLong: Long = num.toLong()  // 코틀린은 모든 원시 타입에 대한 변환 함수를 제공한다.

/**
 * Any, Any?
 * 자바에서 Object 가 클래스 계층의 최상위 타입이듯 코틀린에서는 Any 타입이 모든 null 이 될수 없는 타입의 조상 타입이다.
 * 하지만 자바에서는 참조 타입만 Object 를 정점으로 하는 타입 계층에 포함되며, 원시 타입은 그런 계층에 들어있지 않다.
 * 하지만 코틀린에서는 Any 가 Int 등의 원시 타입을 포함한 모든 타입의 조상 타입이다.
 * null 을 포함하는 변수를 선언하려면 Any? 타입을 사용해야 한다.
 */

/**
 * Unit 타입 : 코틀린의 void
 * 코틀린 Unit 타입은 자바 void 와 같은 기능을 한다.
 * 차이는 무엇일까?
 * Unit 은 모든 기능을 갖는 일반적인 타입이며, void와 달리 Unit을 타입 인자로 쓸 수 있다.
 * Unit 타입에 속한 값은 단 하나뿐이며, 그 이름도 Unit 이다.
 * Unit 타입의 함수는 Unit 값을 묵시적으로 반환한다.
 * 이 두 특성은 제네릭 파라미터를 반환하는 함수를 오버라이드히먄서 반환 타입으로 Unit 을 쓸 때 유용하다.
 * Void 타입을 사용하면 함수를 반환할 때도 void 가 아니기 때문에 return 만이 아닌 return null 을 명시해줘야 한다.
 */
fun f(): Unit {

}

interface Processor<T>{
    fun process(): T
}

class NoResultProcessor : Processor<Unit> {
    override fun process() {
        TODO("Not yet implemented")
        // return 을 명시해 줄 필요 없다
    }
}

/**
 * Nothing 타입 : 이 함수는 결코 정상적으로 끝나지 않는다
 * 코틀린에는 결코 성공적으로 값을 돌려주는 일이 없으므로 반환 값이라는 개념 자체가 의미 없는 함수가 일부 존재한다.
 * 함수가 정상적으로 끝나지 않음을 표현하기 위해
 */
fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}

fun main(args: Array<String>) {
    showProgress(1232132121)

    // equals  메서드는 그 안에 들어있는 값이 아니라 박스 타입 객체를 비교한다.
    val intVal: Int = 1
    val longVal: Long = 1
    println(intVal.equals(longVal)) // false

    val x = 1
    val list = listOf(1L, 2L, 3L)
//    println(x in list) // false
    println(x.toLong() in list) // true
}