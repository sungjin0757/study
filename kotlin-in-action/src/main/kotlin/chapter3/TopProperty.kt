package chapter3

/**
 * 최상위 프로퍼티
 * 함수와 마찬가지로 프로퍼티도 파일의 최상위 수준에 높을 수 있다. 어떤 데이터를 클래스 밖에 위치 시켜야 하는 경우는 흔하지는 않지만, 그래도 가끔 융용하다.
 * 프로퍼티의 값은 정적 필드에 저장된다.
 * 최상위 프로퍼티를 활용해 코드에 상수를 추가할 수 있다.
 * 다른 모든 프로퍼티처럼 접근자 메서드를 통해 자바코드에 노출된다. (var 게터, 세터   val 게터)
 * 더 자연스레 상수를 이용하려면 (public static final) const 변경자를 추가하면 된다.
 */
var opCount = 0; // 최상위 프로퍼티

fun performOperation() {
    opCount++ // 최상위 프로퍼티의 값을 변경한다.
}

fun reportOperation() {
    println("Operation performed $opCount times") // 최상위 프로퍼티 값을 읽는다.
}


