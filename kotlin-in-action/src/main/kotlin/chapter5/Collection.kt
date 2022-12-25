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
val peoples =listOf(Person("Alice", 29), Person("Bob", 31))
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

fun main(args: Array<String>) {
    println(listFilter)
    println(peopleFilter)
    println(listMap)
    println(peopleMap)
    println(peopleFilterMap)
    println(theOldest7)
    println(testMapValues)
}