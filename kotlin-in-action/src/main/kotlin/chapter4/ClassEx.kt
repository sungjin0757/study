package chapter4

import chapter1.Person
import java.io.File
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.util.jar.Attributes
import javax.naming.Context

/**
 * open, final, abstract 변경자 : 기본적으로 final
 * 자바에서는 final 로 명시적으로 상속을 금지하지 않는 모든 클래스를 다른 클래스가 상속할 수 있다.
 * 이렇게 기본적으로 상속이 가능하면 편리한 경우도 많지만 문제가 생기는 경우도 많다.
 * 취약한 기반 클래스 : 하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스를 변경함으로써 개쪄버린 경우
 * 코틀린의 클래스와 메서드는 기본적으로 final이다.
 * 어떤 클래스의 상속을 허용하려면 open 변경자를 붙여야 한다. 그와 더불어 오버라이드를 허용하고 싶은 메서드나 프로퍼티의 앞에도 open 변경자를
 * 붙여야 한다.
 */
open class RichButton: Clickable { // 이 클래스는 열려있다. 다른 클래스가 이 클래스를 상속할 수 있다.
    fun disable() {} // final 이므로 오버라이드 불가능하다.
    open fun animate() {} // 이 함수는 열려있다. 오버라이드 가능하다.
    override fun click() {} // 이 함수는 열려있는 메서드를 오버라이드 한다. 오버라이드한 메서드는 기본적으로 열려있다.
    // 기본적으로 열려있는 클래스를 닫으려면 final 을 붙이면 된다.
    // final override fun click
}

/**
 * 추상클래스
 * 코틀린에서도 추상클래스를 사용할 수있다.
 * abstract 가 붙어 있으면 open 을 따로 붙일 필요가 없다.
 */
abstract class Animated {
    abstract fun animate()
    open fun stopAnimating () {

    }
    fun animateTwice() {  // final

    }
}


/**
 * 가시성 변경자 : 기본적으로 공개
 * 가시성 변경자는 코드 기반에 있는 선언에 대한 클래스 외부 접근을 제어한다.
 * 어떤 클래스의 구현에 대한 접근을 제한함으로써 그 클래스에 의존하는 외부 코드를 깨지 않고도 클래스 내부 구현을 변경할 수 있다.
 * 자바의 기본 가시성인 패키지 전용은 코틀린에 없다. 코틀린은 패키지를 네임스페이스를 관리하기 위한 용도로로만 사용한다.
 * 그래서 패키지를 가시성 제어에 사용하지 않는다.
 * 패키지 전용 가시성에 대한 대안으로 코틀린에는 internal 이라는 새로운 가시성 변경자를 도입했다. internal 은 모듈 내부에서만 볼 수 있다는 뜻이다.
 * 모듈은 한 번에 한꺼번에 컴파일되는 코틀린 파일들을 의미한다.
 * 코틀린에서는 최상위 선언에 대해 private 가시성을 허용한다.
 * 최상위 선언에는 클래스, 함수, 프로퍼티 등이 포함된다.
 */

internal open class TalkativeButton: Forcusable {
    private fun yell() {
        println("Hey!")
    }
    protected fun whisper() {
        println("Let's talk!")
    }
}

/**
 * 컴파일 에러 발생
 * 코틀린은 호출자보다 가시성이 낮은 타입을 참조하지 못한다.
 * 이는 어떤 클래스의 기반 타입 목록에 들어있는 타입이나 제네릭 클래스의 타입 파라미터에 들어있는 타입의 가시성은 그 클래스 자신의 가시성과
 * 같거나 더 높아야 하고, 메서드의 시그니처에 사용된 모든 타입의 가시성은 그 메서드의 가시성과 같거나 더 높아야 한다는 일반적인 규칙에 해당한다.
 * 자바에서는 같은 패키지 안에서 protected 멤버에 접근할 수 있지만, 코틀린에서 그렇지 않다.
 */
//fun TalkativeButton.giveSpeech() {   // 오류 public 멤버가 자신의 internal 수신 타입인 TalkativeButton 을 노출
//    yell() // 접근 불가 private 타입이기 때문
//    whisper() // 접근 불가 protected 타입이기 때문
//}


/**
 * 내부 클래스와 중첩된 클래스 : 기본적으로 중첩 클래스
 * 자바처럼 코틀린에서도 클래스 안에 다른 클래스를 선언할 수 있다. 클래스 안에 다른 클래스를 선언하면 도우미 클래스를 캡슐화하거나
 * 코드 정의를 그 코드를 사용하는 곳 가까이에 두고 싶을 때 유용하다.
 * 자바와의 차이는 코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다는 점이다.
 */
interface State: Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State)
}

class Button2: View {
    override fun getCurrentState(): State {
        return ButtonState()
    }

    override fun restoreState(state: State) {
        TODO("Not yet implemented")
    }

    /**
     * 코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다.
     * 이를 내부 클래스로 변경해서 바깥쪽 클래스에 대한 참조를 초함하게 만들고 싶다면 inner 변경자를 붙여아한다.
     */
    class ButtonState: State {

    }

    inner class Inner {
        fun getOuterClass(): Button2 {
            return this@Button2 // inner 클래스 안에서 outer 클래스를 참조하기 위해서는 this@outer 를 통해 참조할 수 있다.
        }
    }
}

/**
 * 봉인된 클래스 : 클래스 계층 정의 시 계층 확장 제안
 */
//interface Expr
//class Num(val value: Int): Expr
//class Sum(val left: Expr, val right: Expr): Expr
//
//fun eval(e: Expr): Int {
//    when(e) {
//        is Num -> return e.value
//        is Sum -> return eval(e.right) + eval(e.left)
//        else -> throw IllegalArgumentException("Unknown expression") // 디폴트 분기
//    }
//}
// 항상 디폴트 분기를 추가하는 게 편하지는 않다.

/**
 * 상위 클래스에 sealed 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.
 * sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 한다.
 * when 식에서 sealed 클래스의 모든 하위 클래스를 처리한다면 디폴트 분기가 필요 없다.
 * 내부적으로 Expr 클래스는 private 생성자를 가진다. 그 생성자는 클래스 내부에서만 호출할 수 있다.
 * sealed 인터페이스를 정의할 수는 없다. 자바쪽에서 구현하지 못하게 막을 수 없기 때문이다.
 */
sealed class Expr {
    class Num(val value: Int): Expr()
    class Sum(val left: Expr, val right: Expr): Expr()
}

fun eval2(e: Expr): Int {
    when(e) {
        is Expr.Num -> return e.value
        is Expr.Sum -> return eval2(e.right) + eval2(e.left)
    }
}

/**
 * 클래스 초기화
 */

/**
 * 이렇게 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 부른다.
 * 주 생성자는 생성자 파라미터를 지정하고 그 생성자 파라미터에 의해 초기화되는 프로퍼티를 정의하는 두 가지 목적에 쓰인다.
 */
//class User(val nickname: String)
class User constructor(_nickname: String) {
    val nickname: String
    init {  // init 키워드는 초기화 블록을 시작한다. 초기화 블록에는 클래스의 객체가 만들어질 때 실행될 초기화 코드가 들어간다.
        this.nickname = _nickname
    }
}

/**
 * 모든 생성자 파라미터에 디폴트 값을 지정하면 컴파일러가 자동으로 파라미터가 없는 생성자를 만들어준다. 그렇게 자동으로 만들어진 파라미터 없는 생성자는
 * 디폴트 값을 사용해 클래스를 초기화한다.
 */

/**
 * 클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야할 필요가 있다. 기반 클래스를 초기화 하려면 기반 클래스 이름 뒤에 괄호를 치고 생성자 인자를 넘긴다.
 * super 와 같네요
 */
open class User2(val nickname: String){

}

class TwitterUser(nickname: String): User2(nickname) {

}

// 기반 클래스에 인자가 없는 경우에도 생성자를 구현해야한다.
abstract class Ac
class Bc: Ac()

/**
 * 어떤 클래스를 클래스 외부에서 인스턴스화 하지 못하개 막고 싶다면 모든 생성자를 private 하게 만들면 된다.
 */
class Secretive private constructor()


/**
 * 부 생성자 : 상위 클래스를 다른 방식으로 초기화
 * 일반적으로 코틀린에서는 생성자가 여럿 있는 경우가 자바보다 훨씬 적다.
 * 자바에서 오버로드한 생성자가 필효앟ㄴ 상황 중 상당수는 코틀린의 디폴트 파라미터 값가 이름 붙인 인자 문법을 사용해 해결할 수 있다.
 * 그럼에도 생성자가 여럿 필요한 경우에 부 생성자를 사용한다.
 */
open class Parent {
    constructor(ctx: Context) {

    }

    constructor(ctx: Context, attr: Attributes) {

    }
}

class Child: Parent {
    constructor(ctx: Context): super(ctx) {

    }

    constructor(ctx: Context, attr: Attributes): super(ctx, attr){

    }
 }

// 접근자의 가시성 변경
class LengthCounter {
    var counter: Int = 0
        private set
    fun addWord(word: String) {
        counter += word.length
    }
}

/**
 * 컴파일러가 생성한 메서드 : 데이터 클래스와 클래스 위임
 * 자바 플랫폼에서는 클래스가 equals, hashCode, toString 등의 메서드를 구현해야 한다.
 * 코틀린 컴파일러는 이런 메서드를 기계적으로 생성하는 작업을 보이지 않는 곳에서 해준다.
 * 자바와 마찬가지로 코틀린 클래스도 오버라이드 할 수 있다.
 * data 라는 변경자를 클래스 앞에 붙이면 필요한 메서드를 컴파일러가 자동으로 만들어준다.
 */
data class DClient(val name: String, val postalCode: Int)

/**
 * 인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 명시할 수 있다.
 */
class DelegatingCollection<T> (
    innerList: Collection<T> = ArrayList<T>()
        ): Collection<T> by innerList { }

class CountingSet<T> (
    val innerSet: MutableCollection<T> = HashSet<T>()
): MutableCollection<T> by innerSet {
    var objectsAdded: Int = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}

/**
 * object 키워드 : 클래스 선언과 인스턴스 생성
 * 코틀린에서는 object 키워드를 다양한 상황에서 사용하지만 모든 경우 클래스를 정이하면서 동시에 인스턴스를 생성한다는 공통점이 있다.
 * object 키워드를 사용하는 항황은 다음과 같다.
 * 객체 선언(object declaration) 은 싱글턴을 정의하는 방법 중 하나이다.
 * 동반 객체(companion object) 는 인스턴스 메서드는 아니지만 어떤 클래스와 관련 있는 메서드와 팩토리 메서드를 담을 때 쓰인다. 동반 객체 메서드에 접근할 때는
 * 동반 객체가 포함된 클래스의 이름을 사용할 수 있다.
 * 객체 식은 자바의 무명 내부 클래스 대신 쓰인다.
 */

/**
 * 싱글턴을 쉽게 만들기
 * 객체지향 시스템을 설계하다 보면 인스턴스가 하나만 필요한 클래스가 유용한 경우가 많다.
 * 코틀린은 객체 선언 기능을 통해 싱글턴을 언어에서 기본 지원한다. 객체 선언은 클래스 선언과 그 클래스에 속한 단일 인스턴스의 선언을 합친 선언어이다.
**/
object Payroll {
    val allEmployees = arrayListOf<Person>()
    fun calculateSalary() {
        for(person in allEmployees) {

        }
    }
}

/**
 * 객체 선언도 클래스나 인터페이스를 상속할 수 있다. 프레임워크를 사용하기 위해 특정 인터페이스를 구현해야 하는데, 그 구현 내부에 다른 상태가
 * 필요하지 않은 경우에 이런 기능이 유용하다.
 */
object CaseInsensitiveFileComparator: Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}

/**
 * 동반 객체 : 팩토리 메서드와 정적 멤버가 들어갈 장소
 * 코틀린 클래스 안에는 정적인 멤버가 없다. 코틀린 언어는 자바 static 키워드를 지원하지 않는다.
 * 그 대신 코틀린에서는 패키지 수준의 최상위 함수와 객체 선언을 활용한다.
 * 대부분의 경우 최상위 함수를 활용하는 편을 더 권장한다. 하지만 최상위 함수는 private 으로 표시된 비공개 멤버에 접근할 수 없다.
 * 그래서 클래스의 인스턴스와 관계없이 호출해야 하지만 클래스 내부 정보에 접근해야 하는 함수가 필요할 때는 클래스에 중첩된 객체 선언의 멤버 함수로
 * 정의 해야한다. 그런 함수의 대표적인 예로는 팩토리 메서드를 들 수 있다.
 *
 * 클래스 안에 정의된 객체 중 하나에 companion 이라는 특별한 표시를 붙이면 그 클래스의 동반 객체로 만들 수 있다. 동반 객체의 프로퍼티나 메서드에 접근하려면
 * 그 동반 객체가 정의된 클래스 이름을 사용해야한다.
 * 이때 객체의 이름을 따로 지정할 필요가 없다.
 */
 class A {
     companion object {
         fun bar() {
             println("Companion object called")
         }
     }
 }

class B private constructor(val num: Int = 1) {
    // 정적 팩터리 메서드
    companion object {
        fun createB(num: Int): B {
            return B(num);
        }

        fun createEmpty(): B {
            return B()
        }
    }
}

/**
 * 동반 객체를 일반 객체처럼 사용
 * 동반 객체는 클래스 안에 정의된 일반 객체다.
 * 따라서 동반 객체에 이름을 붙이거나, 동반 객체가 인터페이스를 상속하거나, 동반 객체 안에 확장 함수와 프로퍼티를 정의 할 수 있다.
 */
data class Member(val name: String) {
    companion object Loader {
        fun fromJson(jsonText: String): Member {
            return Member("test")
        }
    }
}

/**
 * 동반 객체에서 인터페이스 구현
 * 다른 객체 선언과 마찬가지로 동반 객체도 인터페이스를 구현할 수 있다.
 */
interface JsonFactory<T> {
    fun fromJson(jsonText: String): T
}

data class Member2(val name: String) {
    companion object Loader: JsonFactory<Member2> {
        override fun fromJson(jsonText: String): Member2 {
            return Member2("test")
        }
    }
}

/**
 * 동반 객체 확장
 * 동반 객체에 대한 확장 함수 정의하기
 */
data class Member3(val name: String) {
    companion object {

    }
}

fun Member3.Companion.fromJson(json: String): Member3 {
    return Member3("test")
}

/**
 * 객체 식 : 무명 내부 클래스를 다른 방식으로 작성
 * object 키워드를 싱글턴과 같은 객체를 정의하고 그 객체에 이름을 붙일 때만 사용하지는 않는다.
 * 무명 객체를 정의할 때도 object 키워드를 쓴다.
 * 자바와 달리 final 이 아닌 변수도 객체 식 안에서 사용할 수 있다.
 */
interface AnonymousTest {
    fun testMethod()
}

fun main(args: Array<String>) {
    val client1 = DClient("a", 1)
    val client2 = DClient("a", 1)

    println(client1 == client2)
    println(client1.hashCode() == client2.hashCode())
    println(client1.toString())
    println(client2.toString())
    val client3 = client1.copy() // data class 는 copy 메서드도 지원한다.

    // companion object
    A.bar()
    val member = Member.Loader.fromJson("sdf")
    println(member)

    // 무명 객체
    val testObject = object: AnonymousTest {
        override fun testMethod() {
            TODO("Not yet implemented")
        }
    }
}