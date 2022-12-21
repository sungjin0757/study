package chapter3

/**
 * 최상위 함수
 * 함수를 직접 소스파일의 최상위 수준, 모두 다른 클래스의 밖에 위치시키자.
 * 그런 함수들은 여전히 그 파일의 맨 앞에 정의된 패키지의 멤버 함수 이므로 다른 패키지에서 그 함수를 사용하고 싶을 때는 그 함수가 정의된 패키지를 임포트 해야만 한다.
 *
 * JVM 이 클래스 안에 들어있는 코드만을 실행할 수 있기 때문에 컴파일러는 이파일을 컴파일 할 때 새로운 클래스를 정의해준다.
 * 코틀린 컴파일러가 생성하는 클래스의 이름은 최상위 함수가 들어있던 코틀린 소스파일의 이름과 대응한다. 코틀린 파일의 모든 최상위 함수는 이 클래스의 정적인 메서드가 된다.
 * 따라서 자바에서 joinToString을 호출하기는 쉽다.
 */
fun<T> joinToString3(collection: Collection<T>,
                     separator: String = ",", prefix: String = "", postfix: String = ""): String {
    val result = StringBuilder(prefix)

    for((index, element) in collection.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

/*
컴파일되는 예를 보여준다
public class TopFunction {
    pubilc static String joinToString(...) {...}
}
 */