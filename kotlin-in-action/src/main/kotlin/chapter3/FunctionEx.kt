package chapter3

/**
 * 함수를 호출하기 쉽게 만들기
 */

// default toString 이 아닌 custom 한 toString 을 만들어보자.
// 이 함수는 번잡해 보인다. 어떻게 하면 덜 번잡하게 만들 수 있을까
fun<T> joinToString(collection: Collection<T>,
separator: String, prefix: String, postfix: String): String {
    val result = StringBuilder(prefix)

    for((index, element) in collection.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

/**
 * default 파라미터 값
 * 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있다.
 * 디폴트 값을 통해 많은 수의 오버로딩을 피할 수있다.
 */
fun<T> joinToString2(collection: Collection<T>,
separator: String = ",", prefix: String = "", postfix: String = ""): String {
    val result = StringBuilder(prefix)

    for((index, element) in collection.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun main(args: Array<String>) {
    val list = listOf(1, 2, 3)
    println(list) // toString 호출

    // 호출 시 파라미터의 이름을 명시적으로 붙임으로서 가독성을 높일 수 있다.
    joinToString(list, separator = ",", prefix = "(", postfix = ")")
}