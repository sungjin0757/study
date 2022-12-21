package chapter2

import java.io.BufferedReader
import java.io.IOException
import kotlin.NumberFormatException

/**
 * 코틀린의 예외 처리
 * 자바나 다른 언어의 예외 처리와 비슷하다.
 * 함수를 호출하는 쪽에서는 그 예외를 잡아 처리할 수 있다. 발생한 예외를 함수 호출 단에서 처리하지 않으면 함수 호출 스택을 거슬러
 * 올라가면서 예외를 처리하는 부분이 나올 때 까지 예외를 던진다.
 * 자바 언어와 가장 큰 차이는 throws 가 없다는 점이다. throws 란 체크 예외를 catch 로 처리하지 않은 예외를 호출자에게 넘겨주기 위해서 사용하는 것이다.
 * 다른 최신 JVM 언어와 마찬가지로 코틀린도 체크 예외와 언체크 예외를 구별하지 않는다.
 * 코틀린에서는 함수가 던지는 예외를 지정하지 않고 발생한 예외를 잡아내도 되고 잡아내지 않아도 된다.
 */
fun readNumber(reader: BufferedReader): Int? {
    try {
        val line = reader.readLine()
        return Integer.parseInt(line)
    } catch (e: NumberFormatException) {
        return null
    } finally {
        reader.close()
    }
}

// try 를 식으로 사용
fun readNumber2(reader: BufferedReader) {
    val number = try {
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        return   // 예외가 발생한 경우 catch 이후의 코드는 실행하지 않는다.
    }
    println(number)
}

fun readNumber3(reader: BufferedReader) {
    val number = try {
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        null
    }
    println(number)
}