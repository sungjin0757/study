package chapter7

/**
 * 이항 산술 연산 오버로딩
 * plus 함수 앞에 operator 키워드를 붙여아 한다.
 * times
 * div
 * mod
 * plus
 * minus
 */
data class Point(
    var x: Int,
    var y: Int
) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

/**
 * 코틀린 또한 Comparable 인터페이스를 지원한다.
 * 또한, compareTo 메서드를 호출하는 관례를 지원한다.
 * 비교연산자를 통해서 이다.
 */
class Person(
    val firstName: String,
    val lastName: String
): Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::firstName, Person:: lastName)
    }
}

fun main(array: Array<String>) {
    val p1 = Point(1, 2)
    val p2 = Point(2, 3)
    var p3 = p1 + p2
    p3 += Point(1, 4)
    val p4 = Person("a", "b")
    val p5 = Person("a", "c")
    println(p4 >= p5)
}