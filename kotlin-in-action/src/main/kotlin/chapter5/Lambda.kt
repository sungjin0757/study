package chapter5

/**
 * 람다로 프로그래밍
 * 람다식 또는 람다는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 뜻한다.
 * 람다를 사용하면 쉽세 송통 코드 구조를 라이브러리 함수로 뽑아낼 수 있다.
 * 코틀린 표준 라이브러리는 람다를 아주 많이 사용한다.
 */

/**
 * 람다 소개 : 코드 블록을 함수 인자로 넘기기
 * 이벤트가 발생하면 이 핸들러를 실행하자와 같은 생각을 코드로 표현하기 위해
 * 일련의 동작을 변수에 저장하거나 다른 함수에 넘겨야 하는 경우가 자주 있다.
 * 예전애 자바애서는 무명 내부 클래스를 통해 이런 목적을 달성했다.
 * 이와 달리 함수형 프로그래밍에서는 함수를 값처럼 다루는 접근 방법을 택함으로써 이 문제를 해결한다.
 * 함수형 언어에서는 함수를 직접 다른 함수에 전달할 수 있다.
 */
data class Person(
    val name: String,
    val age: Int
)

// 나이의 최댓값 검색. 컬렉션 직접 사용
fun findTheOldest(peoples: List<Person>): Person? {
    var maxAge = 0;
    var temp: Person? = null
    for(people in peoples) {
        maxAge = Math.max(maxAge, people.age)
        if(maxAge == people.age)
            temp = people
    }

    return temp
}

// 람다를 사용해 컬렉션 검색하기
val people = listOf(Person("a", 1), Person("b", 2))
val theOldest = people.maxBy { it.age }
val theOldest2 = people.maxBy(Person::age)

/**
 * 람다 식의 문법
 * 람다는 값처럼 여기저기 전달할 수 있는 동작의 모음이다.
 * 람다를 따로 선언해서 변수에 저장할 수도 있다.
 * 코틀린 람다 식은 항상 중괄호호 둘러싸여 있다. 인자 목록 주변에 괄호가 없다는 사실을 꼭 기억하라.
 * 화살표가 인자 목록과 람다 본문을 구분해 준다.
 * 람다 식을 변수에 저장할 수 있다. 람다가 저장된 변수를 다른 일반 함수와 마찬가지로 다룰 수 있다.
 */
val sum = {x: Int, y: Int -> x + y}

// 정식으로 람다를 작성하면 다음과 같다
val theOldest3 = people.maxBy ({ p: Person -> p.age }) // 구분자를 너무 많이 써서 번잡하다.
// 코틀린에는 함수 호출 시 맨 뒤에 있는 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다
val theOldest4 = people.maxBy() { p: Person -> p.age}
// 위의 코드 처럼 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다.
val theOldest5 = people.maxBy { p: Person -> p.age }

val names = people.joinToString(" ") {p: Person -> p.name}
// 람다의 파라미터 이름을 디폴트 이름인 it으로 바꾸면 람다 식을 더 간단하게 만들 수 있다.

// 람다를 변수에 저장할 때는 파라미터의 타입을 추론할 문맥이 존재하지 않는다. 따라서 파라미터 타입을 명시해야 한다
val getAge = {p: Person -> p.age}
val theOldest6 = people.maxBy(getAge)

/*
지금까지는 한 문장으로 이뤄진 작은 람다만을 예제로 살펴봤다.
하지만 꼭 한 줄로 이뤄진 작은 람다만 있지는 않다. 본문이 여러 줄로 이뤄진 경우 본문의 맨 마지막에 있는 식이
람다의 결과 값이 된다.
 */
val sum2 = {x: Int, y: Int ->
    println("Computing the sum of $x + $y")
    x + y
}

/**
 * 현재 영역에 있는 변수에 접근
 * 자바 메서드 안에서 무명 내부 클래스를 정의할 때 메서드의 로컬 변수를 무명 내부 클래스에서 사용할 수 있다.
 * 람다 안에서도 같은 일을 할 수 있다.
 * 이런 기능을 보여주기 위해 forEach 표준 함수를 사용해보자.
 */
fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}

/**
 * 코틀린에서는 자바와 달리 람다에서 람다 밖 함수에 있는 파이널이 아닌 변수에 접근할 수 있고, 그 변수를 변경할 수 도 있다.
 * 기본적으로 함수 안에 정의된 로컬 변수의 생명 주기는 함수가 반환되면 끝난다.
 * 하지만 어떤 함수가 자신의 로컬 변수를 포획한 람다를 반환하거나 다른 변수에 저장한다면 로컬 변수의 생명주기와 함수의 생명 주기가 달라질 수도 있다.
 * 포획한 변수가 있는 람다를 저장해서 함수가 끝난 뒤에 실행헤도 람다의 본문 코드는 여전히 변수를 읽거나 쓸 수 있다.
 * 파이널 변수를 포획한 경우에는 람다 코드를 변수 값과 함께 저장한다.
 * 파이널이 아닌 변수를 포획한 경우에는 변수를 특별히 래퍼로 감싸서 나중에 변경하거나 읽을 수 있게 한 다음, 래퍼에 대한 참조를 람다 코드와 함께 저장한다.
 */
fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++
        } else if (it.startsWith("5"))
            serverErrors++
    }
}

/**
 * 멤버 참조
 * 람다를 사용해 코드 블록을 다른 함수에게 인자로 넘기는 방법을 살펴봤다. 하지만 넘기려는 코드가
 * 이미 함수로 선언된 경우는 어떻게 해야 할까? 물론 그 함수를 호출하는 람다를 만들면 된다.
 * 하지만 이는 중복이다. 함수를 직접 넘길수는 없을까
 * 코틀린에서는 자바8과 마찬가지로 함수를 값으로 바꿀 수 있다.
 * 멤버 참조는 프로퍼티나 메서드를 단 하나만 호출하느 함수 값을 만들어준다.
 */
val getAge2 = Person::age

// 최상위에 선언된 하무나 프로퍼티를 참조할 수도 있다.
fun test() = println("test")
val test2 = ::test

val action = {
    person: Person, message: String -> test()
}

val nextAction = ::action

// 생성자 참조를 사용하면 클래스 생성 작업을 연기하거나 저장해둘 수 있다.
val createPerson = ::Person
val p = createPerson("a", 1)

//확장 함수도 멤버 함수와 똑같은 방식으로 참조할 수 있다는 점을 기억하가.
fun Person.isAdult(): Boolean {
    return age >= 21
}
val predicate = Person::isAdult

/**
 * 수신 객체 지정 람다 : with 와 apply
 * 자바의 람다에는 없는 코틀린 람다의 독특한 기능을 설명한다.
 * 그 기능은 바로 수신 객체를 명시하지 않고 람다의 본문 안에서 다른 객체의 메서드를 호출할 수 있게 하는 것이다.
 * 그런 람다를 수신 객체 지정 람다라고 부른다.
 */

/**
 * with 함수
 * 어떤 객체의 이름을 반복하지 않고도 그 객체에 대한 다양한 연산을 수행할 수 있다면 좋을 것이다.
 * 코틀린은 이러한 기능을 with 라는 라이브러리 함수를 통해 제공한다.
 * 첫 번째 인자로 받은 객체를 두 번째 인자로 받은 람다의 수신 객체로 만든다.
 * 인자로 받은 람다 본문에서는 this를 사용해 그 수신 객체에 접근할 수 있다.
 * 일반적인 this 와 마찬가지로 this와 점을 사용하지 않고 프로퍼티나 메서드 이름만 사용해도 수신 객체의 멤버에 접근할 수 있다.
 * with 가 반환하는 값은 람다 코드를 실행한 결과며, 그 결과는 람다 식의 본문에 있는 마지막 식의 값이다.
 * 하지만 때로는 람다의 결과 대신 수신 객체가 필요한 경우도 있다.
 * 그럴 때는 apply 함수를 사용할 수 있다.
 */
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A' .. 'Z') {
        result.append(letter)
    }
    result.append("\nNow I know the alphabet")
    return result.toString()
}

fun alphabetVWith(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for(letter in 'A' .. 'Z') {
            this.append(letter)
        }
        this.append("\nNow I know the alphabet")
        this.toString()
    }
}

fun alphabetVWithRefactoring(): String {
    return with(StringBuilder()) {
        for(letter in 'A' .. 'Z') {
            this.append(letter)
        }
        this.append("\nNow I know the alphabet")
        this.toString()
    }
}

/**
 * apply 함수
 * 거의 with 와 동일하다. 유일한 차이란 apply는 항상 자신에게 전달된 객체를 반환한다는 점 뿐이다.
 */
fun alphabetVApply(): String {
    return StringBuilder().apply {
        for(letter in 'A' .. 'Z') {
            append(letter)
        }
        append("\nNow I know the alphabet")
    }.toString()
}

fun alphabetVBuildString(): String {
    return buildString {
        for(letter in 'A' .. 'Z') {
            append(letter)
        }
        append("\nNow I know the alphabet")
    }
}

fun main(args: Array<String>) {
    { println(42) }() // 람다 식을 직접 호출
    run { println(42) }  // run 을 사용하는 것이 더욱 가독력에도 좋다.
}
