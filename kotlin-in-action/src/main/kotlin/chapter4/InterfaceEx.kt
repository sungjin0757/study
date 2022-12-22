package chapter4

/**
 * 인터페이스
 * 코틀린 인터페이스는 자바 8 인터페이스와 비슷하다. 코틀린 인터페이스 안에는 추상 메서드뿐 아니라 구현이 있는 메서드도 정의할 수 있다.
 * 다만 인터페이스에는 아무런 상태도 들어갈 수 없다.
 * 인터페이스 멤버는 항상 열려 있으며 final 로 변경할 수 없다. 인터페이스 멤버에게 본문이 없으면 자동으로 추상 멤버가 되지만, 그렇더라도
 * 따로 멤버 선언 앞에 abstract 키워드를 덧붙일 필요가 없다.
 */
interface Clickable {
    fun click()
    fun showOff() {
        println("I'm Clikable!")
    }
}

interface Forcusable {
    fun setFocus(b: Boolean) {
        println("I ${if (b) "got" else "lost"} focus.")
    }
    fun showOff() {
        println("I'm focusable!")
    }
}

/**
 * 자바에서는 extends 와 implements 키워드를 사용하지만, 코틀린에서는 클래스 이름 뒤에 클론을 붙이고 인터페이스와 클래스 이름을 적는 것으로
 * 클래스 확장과 인터페이스 구현을 모두 처리한다.
 * 자바와 마찬가지로 클래스는 인터페이스를 원하는 만큼 개수 제한 없이 마음대로 구현할 수 있지만, 클래스는 오직 하나만 확장할 수 있다.
 */
class Button: Clickable, Forcusable {  // 오버라이딩 메서드를 직접 정의하지 않으면 같은 메서드에 대한 오버라이드는 오류가 발생한다.
    // override 변경자를 꼭 사용해야 한다.
    override fun click() {
        println("I was clicked")
    }

    // 코틀린 컴파일러는 두 메서드를 아우르는 구현을 하위 클래스에 직접 구현하게 강제한다.
    override fun showOff() {
        super<Clickable>.showOff()
        super<Forcusable>.showOff()
    }
}


/**
 * 인터페이스에 선언된 프로퍼티 구현
 * 코틀린에서는 인터페이스에 추상 프로퍼티 선언을 넣을 수 있다.
 */
interface IUser {
    val nickname: String
//    val email: String
//        get() {
//            return email.substringBefore("@");
//        }
}

class PrivateUser(override val nickname: String): IUser

class SubscribingUser(val email: String): IUser {
    override val nickname: String
        get() {
            return email.substringBefore('@')
        }
}