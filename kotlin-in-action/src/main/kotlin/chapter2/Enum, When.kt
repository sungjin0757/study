package chapter2


/**
 * enum 클래스 정의
 * 코틀린에서 enum 은 소프트 키워드라 부르는 존재.
 * enum 은 클래스 앞에 있을 때는 특별한 의미를 지니지만 다른 곳에서는 이름에 사용할 수 있다.
 * 반면 class 는 키워드이다. 따라서 class 라는 이름을 사용할 수 없다.
 */
enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}

// 프로퍼티와 메소드를 enum 안에 선언하는 방법
enum class Color2(
    val r: Int, val g: Int, val b: Int
) {
    RED(255, 0, 0), ORANGE(255, 166, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0),
    BLUE(0, 0, 255), INDIGO(75, 0, 130), VIOLET(238, 13, 238);

    fun getRgb(): Int {
        return (r * 256 + g) * 256 + b
    }
}

/**
 * when 으로 enum 클래스 다루기
 * switch 에 해당하는 구성요소는 when 이다.
 * if 와 마찬가지로 when 도 값을 만들어 내는 식이다.
 */
fun getMnemonic(color: Color2) =
    when (color) {
        Color2.RED -> "Richard"
        Color2.ORANGE -> "Of"
        Color2.YELLOW -> "York"
        Color2.GREEN -> "Gave"
        Color2.BLUE -> "Battle"
        Color2.INDIGO -> "In"
        Color2.VIOLET -> "Vain"
    }

/**
 * when 과 임의의 객체를 함께 사용
 * 코틀린에서 when 은 자바의 switch 보다 훨씬 강력하다. 분기 조건에 상수만을 사용할 수있는 자바 switch와 달리 코틀린 when의 분기 조건은
 * 임의의 객체를 허용한다.
 */
fun mix(c1: Color2, c2: Color2) =
    when(setOf(c1, c2)) {                                 // setOf 와 분기조건에 있는 객체 사이를 매치할 때 동등성을 사용한다.
        setOf(Color2.RED, Color2.YELLOW) -> Color2.ORANGE
        setOf(Color2.YELLOW, Color2.BLUE) -> Color2.GREEN
        setOf(Color2.BLUE, Color2.VIOLET) -> Color2.INDIGO
        else -> throw Exception("Dirty Color")
    }

/**
 * 스마트 캐스트 : 타입 검사와 타입 캐스트를 조합
 */
interface Expr
class Num(val value: Int): Expr
class Sum(val left: Expr, val right: Expr): Expr

/**
 * 코틀린에서는 is 를 사용하여 타입을 검사한다. 자바의 instanceof 와 동일하다.
 * 하지만, 자바에서는 instanceof 를 사용하면 또 타입 캐스트 과정을 진행해야한다.
 * 코틀린에서의 is 는 타입 검사를 통과하게 되면 컴파일러가 캐스팅 까지 진행하여준다. 이를 스마트 캐스트이라 한다.
 * 스마트 캐스트는 is 로 변수에 든 값의 타입을 검사한 다음에 그 값이 바뀔 수 없는 경우에만 작동한다.
 * 즉, 프로퍼티가 반드시 val이어야 한다.
 */
fun evalIf(e: Expr): Int {
    if(e is Num) {
        return e.value
    }
    if(e is Sum) {
        return evalIf(e.right) + evalIf(e.left)
    }
    throw IllegalArgumentException("Unknown exception")
}

// 리팩토링 if 를 when 으로 변경
fun evalWhen(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> evalWhen(e.right) + evalWhen(e.left)
        else -> throw IllegalArgumentException("Unknown exception")
    }

// if 와 when 분기에서 블록 사용
fun evalWithLogging(e: Expr): Int =
    when(e) {
        is Num -> {
            println("num: ${e.value}")
            e.value
        }
        is Sum -> {
            val left = evalWithLogging(e.left)
            val right = evalWithLogging(e.right)
            println("sum : ${left} + ${right}")
            left + right
        }
        else -> throw IllegalArgumentException("Unknown exception")
    }