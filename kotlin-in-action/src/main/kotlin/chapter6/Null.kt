package chapter6

import java.lang.IllegalArgumentException
import java.util.*

/**
 * 코틀린 타입 시스템
 */

/**
 * null 가능성
 * null 가능성은 NullPointerException 오류를 피할 수 있게 돕기 위한 코틀린 타입 시스템의 특성이다.
 * 코틀린을 비롯한 최신 언어에서 null 에 대한 접근 방법은 가능한 문제를 실행 시점에서 컴파일 시점으로 옮기는 것이다.
 * null 이 될 수 있는지 여부를 타입 시스템에 추가함으로써 컴파일러가 여러 가지 오류를 컴파일 시 미리 감지해서 실행 시점에 발생할 수 있는
 * 예외의 가능성을 줄일 수 있다.
 */

/**
 * null 이 될 수 있는 타입
 * 코틀린과 자바의 첫 번째이자 가장 중요한 차이는 코틀린 타입 시스템이 null 이 될 수 있는 타입을 명시적으로 지원한다는 점이다.
 * 어떤 변수가 null 이 될 수 있다면 그 변수에 대해 메서드를 호출하면 NPE 가 발생할 수 있으므로 안전하지 않다.
 * 코틀린은 그런 데서드 호출을 금지함으로써 많은 오류를 방지한다.
 */

// 이 함수에는 null 이 허용되지 않는다. 그러므로, 인자로 실행시점에서 null 을 넣을 수 없다.
fun strLen(s: String): Int {
    return s.length
}

// null 이 될 수 있는 값을 null 이 될 수 없는 변수에 대입할 수 없다.
val x: String? = null
//var y: String = x  // Type mismatch.

// null 과 비교하고 나면 컴파일러는 그 사실을 기억하고 null 이 아님이 확실한 영역에서는 해당 값을 null 이 될 수 없는 타입의 값처럼 사용할 수 있다.
fun strLenSafe(s: String?): Int {
    if (s != null) {
        return s.length
    }
    return 0
}

// null 을 검사할 수 있는 도구가 if 검사뿐이라면 코드가 번잡해지는 일을 피할 수 없ㅇ르 것이다. 다행히 코틀린은 널이 될 수있는 값을 다룰 때 도움이 되는 여러 도구를 제공한다.

/**
 * 실행 시점에 null 이 될 수 있는 타입이나 null 이 될 수 없는 타입의 객체는 같다. null 이 될 수 있는 타입은 null이 될 수 없는 타입을
 * 감싼 래퍼 타입이 아니다.
 * 모든 검사는 컴파일 시점에 수행된다. 따라서 코틀린에서는 null 이 될 수 있는 타입을 처리하는 데 별도의 실행 시점 부가 비용이 들지 않는다.
 */

/**
 * 안전한 호출 연산자: ?.
 * 코틀린이 제송하는 가장 유용한 도구 중 하나가 안전한 호출 연산자인 ?. 이다.
 * ?. 은 null 검사와 메서드 호출을 한 번의 연산으로 수행한다.
 * 예를 들어 s?.toUpperCase() 는 if (s! = null) s.toUpperCase() else null 과 같다
 * 안전한 호출의 결과 타입도 null이 될 수 있는 타입이라는 사실에 유의해야한다.
 */
fun printAllCaps(s: String?): String? {
    return s?.uppercase(Locale.getDefault())
}

// 메서드 호출 뿐 아니라 프로퍼티를 읽거나 쓸 때도 안전한 호출을 사용할 수 있다.
class Employee(val name: String, val manager: Employee?)
fun managerName(employee: Employee): String? {
    return employee.manager?.name
}

class Address(
    val streetAddress: String, val zipCode: Int,
    val city: String, val country: String
)
class Company(val name: String, val address: Address?)
class Person(val name: String, val company: Company?)

fun Person.countryName(): String {
    val country = this.company?.address?.country
    return if(country != null) country else "Unknown"
}

/**
 * 엘비스 연산자 : ?:
 * 코틀린은 null 대신 사용할 디폴트 값을 지정할 때 편리하게 사용할 수 있는 연산자를 제공한다.
 * 그 연산자는 엘비스 연산자라고 한다.
 */
fun foo(s: String?) {
    val t: String = s ?: ""
}

fun strLenSafeVElvis(s: String?): Int {
    return s?.length ?: 0
}

fun printShippingLabel(person: Person) {
    val address = person.company?.address?:throw IllegalArgumentException("No address")
    with(address) {
        println(streetAddress)
        println("$zipCode $city, $country")
    }
}

/**
 * 안전한 캐스트 : as?
 * as? 연산자는 어떤 값을 지정한 타입으로 캐스트한다. as? 는 값을 대상 타입으로 변환할 수 없으면 null 을 반환한다.
 */
class Person2(val firstName: String, val lastName: String) {
    // 파라미터로 받은 값이 원하는 타입인지 쉽게 검사하고 캐스트할 수 있고, 타입이 맞지 않으면 쉽게 false 를 반환할 수 있다.
    override fun equals(o: Any?): Boolean {
        val otherPerson = o as? Person2 ?: return false
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    }

    override fun hashCode(): Int {
        return firstName.hashCode() * 31 + lastName.hashCode()
    }
}

/**
 * null 아님 단언 : !!
 * null 아님 단언은 코틀린에서 null 이 될 수 있는 타입의 값을 다룰 때 사용할 수 있는 도구 중에서 가장 단순하면서도 무딘 도구다.
 * 느낌표를 이중으로 사용하면 어떤 값이든 null 이 될 수없는 타입으로 바꿀 수 있다. 실제 null 에 대해 !! 를 적용하면 NPE가 발생한다.
 */
fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(sNotNull.length)
}

/**
 * let 함수
 * let 함수를 사용하면 null 이 될 수 있는 식을 더 쉽게 다룰 수 있다. let 함수를 안전한 호출 연산자와 함께 사용하면 원하는 식을 평가해서
 * 결과가 null 인지 검사한 다음에 그 결과를 변수에 넣는 작업을 간단한 식을 사용해 한꺼번에 처리할 수 있다.
 * let 함수는 자신의 수신 객체를 인자로 전달받은 람다에게 넘긴다. 널이 될 수 있는 값에 대해 안전한 호출 구문을 사용해 let을 호출하되
 * null 이 될 수 없는 타입을 인자로 받는 람다를 let 에 전달한다.
 * let 함수는 수신 객체가 null 이 아닌 경우에만 호출된다.
 */
fun sendEmailTo(email: String) {
    println("Sending email to $email")
}

/**
 * 나중에 초기화할 프로퍼티
 * 코틀린에서 클래스 안의 null 이 될 수 없는 프로퍼티를 생성자 안에서 초기화하지 않고 특별한 메서드 안에서 초기화할 수는 없다.
 * 코틀린에서는 일반적으로 생성자에서 모든 프로퍼티를 초기화 해야한다. 게다가 프로퍼티 타입이 null 이 될 수 없는 타입이라면 반드시 null
 * 이 아닌 값으로 그 프로퍼티를 초기화 해야한다.
 * 그런 초기화 값을 제공할 수 없으면 null 이 될 수 있는 타입을 사용할 수 밖에 없다. 하지만 null 이 될 수 있는 타입을 사용하면
 * 모든 프로퍼티 접근에 null 검사를 넣거나 !!을 사용해야한다.
 */
class MyService {
    fun performAction(): String {
        return "foo"
    }
}

class MyTest {
    private var myService: MyService? = null

    fun setUp() {
        this.myService = MyService()
    }

    fun testAction() {
        // TODO
    }
}

class MyTestV2 {
    private lateinit var myService: MyService // 초기화하지 않고 null 이 될 수 없는 프로퍼티를 선언한다.
    // 초기화 되기 전에 이 프로퍼티를 사용하면, "lateinit property myService has not been initialized" 라는 예외가 발생한다.
    // 무슨 예외인지 한 눈에 볼 수 있기 때문에, NPE 보다 훨씬 괜찮다.

    fun setUp() {
        this.myService = MyService()
    }

    fun testAction() {
        // TODO
    }
}

/**
 * null 이 될 수 있는 타입 확장
 * null 이 될 수 있는 타입에 대한 확장 함수를 정의하면 null 값을 다루는 강력한 도구로 활용할 수있다.
 * 어떤 메서드를 호출하기 전에 수신 객체 역할을 하는 변수가 null 이 될 수 없다고 보장하는 대신, 직접 변수에 대해 메서드를 호출해도 확장 함수인 메서드가 알아서
 * null 을 처리해준다.
 */
fun verifyUserInput(input: String?) { // null 을 수신 객체로 포함하여도 된다.
    if (input.isNullOrBlank()) {
        println("Please fill in the required fields")
    }
}

// null 이 될 수 있는 타입에 대한 확장을 정의하면 null 이 될 수 있는 값에 대해 그 확장 함수를 호출할 수 있다.
fun String?.isNullOrBlank(): Boolean {
    return this == null || this.isBlank() // 두 번째 this에는 스마트캐스트가 적용된다.
}

// 앞에서 살펴 본 let 함수도 null 이 될 수 있는 타입의 값에 대해 호출할 수 있지만 let 은 this 가 null 인지 검사하지 않는다.

/**
 * 타입 파라미터의 null 가능성
 * 코틀린에서는 함수나 클래스의 모든 타입 파라미터는 기본적으로 null 이 될 수 있다.
 * 따라서 타입 파라미터 T 를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 물음표가 없더라도 T 가 null 이 될 수 있는 타입이다.
 */
fun <T> printHashCode(t: T) {
    println(t?.hashCode())
}

/**
 * 타입 파라미터가 null 이 아님을 확실히 하려면 null 이 될 수 없는 타입 상한을 지정해야 한다.
 * 이렇게 null 이 될 수 없는 타입 상한을 지정하면 null 이 될 수 있는 값을 거부하게 된다.
 */
fun <T: Any> printHashCodeV2(t: T) {
    println(t.hashCode())
}

/**
 * null 가능성과 자바
 * 자바의 @Nullable String 은 코틀린의 String? 와 같다
 * 자바의 @NotNull String 은 String 와 같다
 */

fun main(args: Array<String>) {
    // null 이 될 수 있는 타입의 값을 null 이 될 수 없는 타입의 파라미터를 받는 함수에 전달할 수 없다.
//    strLen(x)

//    ignoreNulls(null) // NPE!

    var email: String? = "a@bc.com"
    email?.let { sendEmailTo(it) }
//    printHashCodeV2(null) // Exception occurred
}