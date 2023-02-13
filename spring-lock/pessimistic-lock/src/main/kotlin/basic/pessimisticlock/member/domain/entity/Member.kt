package basic.pessimisticlock.member.domain.entity

import basic.pessimisticlock.common.model.AbstractTimeEntity
import basic.pessimisticlock.member.domain.value.Age
import basic.pessimisticlock.member.domain.value.MemberId
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "member")
class Member (
    @EmbeddedId
    var id: MemberId?,

    var userName: String,

    @Embedded
    var age: Age
): AbstractTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (userName != other.userName) return false
        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + age.hashCode()
        return result
    }

    companion object {
        fun generate(id: MemberId, userName: String, age: Age): Member {
            verifyAge(age)
            return Member(id, userName, age)
        }

        private fun verifyAge(age: Age) {
            if(age.value <= 0)
                throw IllegalArgumentException("Not Valid Age")
        }
    }
}