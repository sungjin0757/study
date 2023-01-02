package chapter7

import java.time.LocalDate

/**
 * get 관례
 * 인덱스 연산자를 사용해 원소를 읽는 연산은 get 연산자 메서드로 변환되고, 원소를 쓰는 연산은 set 연산제 메서드로 변환된다.
 */
operator fun Point.get(index: Int): Int {
    return when(index) {
        0 -> x
        1 -> y
        else ->
            throw IndexOutOfBoundsException()
    }
}

operator fun Point.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else ->
            throw IndexOutOfBoundsException()
    }
}

/**
 * in 연산자와 대응하는 함수는 contains 다
 */
data class Rectangle(
    val upperLeft: Point,
    val lowRight: Point
) {
    operator fun contains(p: Point): Boolean {
        return p.x in upperLeft.x until lowRight.x &&
                p.y in upperLeft.y until lowRight.y
    }
}

/**
 * for 루프를 위한 iterator 관례
 * 직접 iterator 메서드를 구현할 수 도 있다.
 */
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object: Iterator<LocalDate> {
        var current = start
        override fun hasNext(): Boolean {
            return current <= endInclusive
        }

        override fun next(): LocalDate {
            return current.apply { current = plusDays(1) }
        }
    }

fun main(args: Array<String>) {
    val pp = Point(1, 2)
    print(pp[1])
    pp[1] = 4
}