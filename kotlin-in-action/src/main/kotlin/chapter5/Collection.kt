package chapter5

import java.util.*

/**
 * 컬렉션 함수형 API
 * 함수형 프로그래밍 스타일을 사용하면 컬ㄹ렉션을 다룰 때 편리하다. 대부분의 작업에 라이브러리 함수를 활용할 수 있고 그로 인해 코드를 아주 간결하게 만들 수 있다
 */

/**
 * 필수적인 함수 : filter, map
 * 컬렉션을 활용할 때 기반이 되는 함수다. 대부분의 컬렉션 연산을 이 두 함수를 통해 표형할 수 있다.
 */

val list = listOf(1, 2, 3, 4)
val peoples =listOf(Person("Alice", 29), Person("Bob", 31), Person("a", 29),
Person("b", 20))
val listFilter = list.filter { it -> it % 2 ==0 }
val peopleFilter = peoples.filter {it -> it.age > 30}

// filter 함수는 원소를 변환할 수는 없다. 원소를 변환하려면 map 함수를 사용홰야 한다.
val listMap = list.map { it -> Math.pow(it.toDouble(), 2.0) }
val peopleMap = peoples.map(Person::name)

// filter, map
val peopleFilterMap = peoples.filter{it -> it.age > 30}.map(Person::name)

// 나이가 최댓값인 사람 목록 구하기
val maxAge = peoples.maxBy(Person::age).age
val theOldest7 = peoples.filter{it -> it.age == maxAge}

// 컬렉션이 map 인 경우에도 사용할 수 있다.
val testMap = mapOf(1 to "one", 2 to "two")
val testMapValues = testMap.mapValues { it.value.uppercase(Locale.getDefault()) }

/**
 * all, any, count, find : 컬렉션에 술어 적용
 * 컬렉션에 대해 자주 수행하는 연산으로 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산이 있다.
 * 코틀린에서는 all 과 any 가 이런 연산이다.
 * count 함수는 조건을 만족하는 원소의 개수를 반환하며, find 함수는 조건을 만족하는 첫 번째 원소를 반환한다.
 */
val canBeInClub27 = {p: Person -> p.age <= 27}
val isInClub27All = peoples.all(canBeInClub27)
val isInClub27Any = peoples.any(canBeInClub27)
val inClub27Count = peoples.count(canBeInClub27)

// 원소를 하나 찾고 싶다면 find. firstOrNull 과 동일
val findInClub27 = peoples.find(canBeInClub27)

/**
 * groupBy : 리스트를 여러 그룹으로 이뤄진 맵으로 변경
 * 컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶을 때 사용
 */
val ageGroup = peoples.groupBy{it.age}
val stringList = listOf("a", "ab", "b")
val firstGroup = stringList.groupBy(String::first)

/**
 * flatMap 과 flatten : 중첩된 컬렉션 안의 원소 처리
 * flatMap 함수는 먼저 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다.
 * 리스트의 리스트가 있는데 모든 중첩된 리스트의 원소를 한 리스트로 모아야 한다면 flatMap 을 떠올릴 수 있다.
 * 하지만 특별히 변환해야할 내용이 없다면 리스트의 리스트를 펼치기만 하면 된다.
 * 이때 flatten 을 사용한다.
 */
val strings = listOf("abc", "def")
val stringsFlatMap = strings.flatMap{it.toList()}

class Book(val title: String, val authors: List<String>)
val books = listOf(Book("Thursday Next", listOf("Jasper Fforde")),
Book("Mort", listOf("Terry Pratchett")), Book("Good Omens", listOf("Terry Pratchett", "Neil Gaiman")))
val bookAuthors = books.flatMap { it -> it.authors }.toSet()

/**
 * 지연 계산 컬렉션 연산
 * map 이나 filter 같은 몇 가지 컬렉션 함수를 살펴 봤다.
 * 그런 함수는 결과 컬렉션을 즉시 생성한다.
 * 이는 컬렉션 함수를 연쇄하면 매 단계마다 계산 중산 결과를 새로운 컬렉션에 임시로 담는다는 말이다. 시퀀스를 사용하면 중산 임시 컬렉션을 사용하지 않고도
 * 컬렉션을 연쇄할 수 있다.
 * 코틀린 지연 계산 시퀀스는 Sequence 인터페이스에서 시작한다. 이 인터페이스는 단지 한 번에 하나씩 열거될 수 있는 원소의 시퀀스를 표현할 뿐이다.
 * Sequence 안에는 iterator 라는 단 하나의 메서드가 있다. 그 메서드를 통해 시퀀스로부터 원소 값을 얻을 수 있다.
 */
val nameStartAList = peoples.asSequence()
    .map(Person::name)
    .filter { it -> it.startsWith("A") }
    .toList()

/**
 * 시퀀스 연산 실행 : 중간 연산과 최종 연산
 * 시퀀스에 대한 연산은 중간 연산과 최종 연산으로 나뉜다.
 * 중간 연산은 다른 시퀀스를 변환한다. 그 시퀀스는 최초 시퀀스의 원소를 변환하는 방법을 한다. 최종 연산은 결과를 반환한다.
 * 중간 연산은 항상 지연 계산된다. 최종연산이 없으면 아무 내용도 출력되지 않는다.
 */

/**
 * 시퀀스 만들기
 */
val naturalNumbers = generateSequence(0) {it + 1}
val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }


fun main(args: Array<String>) {
    println(listFilter)
    println(peopleFilter)
    println(listMap)
    println(peopleMap)
    println(peopleFilterMap)
    println(theOldest7)
    println(testMapValues)
    println(isInClub27All)
    println(isInClub27Any)
    println(inClub27Count)
    println(ageGroup)
// {29=[Person(name=Alice, age=29), Person(name=a, age=29)], 31=[Person(name=Bob, age=31)], 20=[Person(name=b, age=20)]}
    println(firstGroup) // {a=[a, ab], b=[b]}
    println(stringsFlatMap) //[a, b, c, d, e, f]
    println(bookAuthors) // [Jasper Fforde, Terry Pratchett, Neil Gaiman]
    println(nameStartAList)
    println(numbersTo100.sum()) // 5050
}