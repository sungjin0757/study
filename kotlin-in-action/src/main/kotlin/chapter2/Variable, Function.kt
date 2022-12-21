package chapter2

/**
 * 함수를 선언 할 때 fun 키워드를 사용
 * 파라미터 이름 뒤에 그 파라미터의 타입을 쓴다.
 * 함수를 최상위 수준에 정의할 수 있음.
 * 배열도 일반적인 클래스와 마찬가지. 배열처리를 위한 문법이 따로 존재하지 않음.
 */
fun main(args: Array<String>) {
    println("Hello, World")

    // 변수, 타입 추론이 이뤄짐.
    val question = "Hello?"; // String
    val answer = 20; // Int
    val yearsToCompete = 7.5e6; // Double
    // 초기화 식을 사용하지 않으려면 반드시 타입을 명시해줘야함
    val num: Int;
    num = 40;

    /**
     * 변경이 가능한 변수와 변경이 불가능한 변수
     * val : 변경 불가능한 참조를 저장하는 변수. final 변수에 해당
     * var : 변경 가능한 참조. 자바의 일반 함수를 뜻함
     * 기본적으로는 모든 변수를 val 키워드를 사용해 불변 변수로 선언하고, 나중에 필요할 때만 var 을 사용하는 것이 좋음.
     */

    // val 참조 자체는 불변일지라도 그 참조가 가리키는 객체의 내부 값은 변경될 수 있다는 사실을 기억해야 한다.
    val languages = arrayListOf("Java");
    languages.add("Kotlin");

    // var 키워드를 사용하면 변수의 값을 변경할 수 있지만 변수의 타입은 고정됨
    var answer2 = 20;
//    answer2 = "string"; // TypeError

}

/**
 * 식이 본문인 함수
 * 본문이 중괄호로 둘럿싸인 함수를 블록이 본문인 함수라 부르고, 등호황 식으로 이뤄진 함수를 식이 본문인 함수라 말한다.
 */
fun max(a: Int, b: Int): Int = if(a > b) a else b;

fun printName1(args: Array<String>) {
    val name = if(args.isNotEmpty()) args[0] else "Kotlin";
    println("Hello, $name"); // 문자열 템플릿. 변수를 문자열 안에 사용할 수 있음.
}

fun printName2(args: Array<String>) {
    println("Hello, ${if(args.isNotEmpty()) args[0] else "Kotlin"}");
}

