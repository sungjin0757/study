package chapter3

import java.lang.IllegalArgumentException

/**
 * 코드 다듬기 : 로컬 함수와 확장
 * 많은 개발자들이 좋은 코드의 중요한 특징 중 하나가 중복이 없는 것이라한다.
 * 이 방법중 가장 많이 사용되는 방식은 메소드 추출이다.
 * 하지만, 메소드 추출은 클래스 안에 작은 메서드가 많아지고 각 메서드 사이의 관계를 파악하기 힘들어서 코드를 이해하기 더 어려워 질 수도 있다.
 * 코틀린에는 더 깔끔한 해법이 있다. 코틀린에서는 함수에서 추출한 함수를 원 함수 내부에 중첩시킬 수 있다. 그렇게 하면 문법적인 부가 비용을 들이지 않고도 깔끔하게
 * 코드를 조작할 수 있다.
 */

class User(
    val id: String,
    val name: String,
    val address: String
)

class UserRepository {
    fun saveUserV1(user: User) {
        // 필드 검증이 중복
        if(user.name.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty name")
        }

        if(user.address.isEmpty()) {
            throw IllegalArgumentException("Can't save user ${user.id}: empty address")
        }
    }

    fun saveUserV2(user: User) {
        fun validate(value: String, fieldName: String) {
            if(value.isEmpty()) {
                throw IllegalArgumentException("Can't save user ${user.id}: empty $fieldName")
            }
        }
    }
}