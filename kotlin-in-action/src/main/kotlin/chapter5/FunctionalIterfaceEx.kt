package chapter5

/**
 * 자바 함수형 인터페이스 활용
 * 함수형 인터페이스를 인자로 원하는 자바 메서드에 코틀린 람다를 전달할 수 있다.
 */

// 람다가 주변 영역의 변수를 포획하면 반복 사용하지 못하고 새로운 객체를 계속 생성한다.
fun handleComputation(id: String) {
    // id 포획
    TestMethod.postponeComputation(1000) {println(id)}
}

/**
 * SAM 생성자 : 람다를 함수형 인터페이스로 명시적으로 변경
 * SAM 생성자는 람다를 함수형 인터페이스의 인스턴스로 변환할 수 있게 컴파일러가 자동으로 생성한 함수이다.
 * 컴파일러가 자동으로 람다를 함수형 인터페이스 무명 클래스로 바꾸지 못하는 경우 SAM 생성자를 사용할 수 있다.
 */
fun createAllDoneRunnable(): Runnable {
    return Runnable {println("All done!")}
}

fun main(args: Array<String>) {
    TestMethod.postponeComputation(1000) {println(42)} // 컴파일러는 람다를 자동으로 Runnable 인스턴스로 변환해준다.
    TestMethod.postponeComputation(1000, object: Runnable {
        override fun run() {
            TODO("Not yet implemented")
        }
    }) // 무명 객체를 사용할 수 있다.
    /**
     * 람다와 무명 객체 사이에는 차이가 있다. 객체를 명시적으로 선언하는 경우 메서드를 호출할 때마다 새로운 객체가 생성된다.
     * 하지만 람다는 다르다. 정의가 들어있는 함수의 변수에 접근하지 않는 람다에 대응하는 무명 객체를 메서드를 호출할 때마다 반복 사용한다.
     */
}