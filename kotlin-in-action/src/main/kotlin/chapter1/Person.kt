package chapter1

/**
 * name 과 age 라는 프로퍼티가 들어간 간단한 데이터 클래스이다.
 */
data class Person(val name: String, val age: Int? = null) {
    fun main(args: Array<String>) {
        val persons = listOf(Person("영희"),
                            Person("철수", 29));
        /**
         * 리스트에 가장 나이가 많은 사람을 찾기 위해 maxBy 함수를 사용한다.
         * it 이라는 이름을 사용하면 람다 식의 유일한 인자를 사용할 수 있다.
         * 엘비스 연산자라고 부르는 ?: 는 null 인 경우 0 을 반환하고, 그렇지 않은 경우 값을 반환한다.
         */
        val oldest = persons.maxBy { it.age ?: 0 };
        println("나이가 가장 많은 사람: $oldest"); // 나이가 가장 많은 사람: Person(name=철수, age=29)
    }
}
