package basic.pessimisticlock.member.domain.value

import javax.persistence.Embeddable
import java.time.LocalDateTime
import java.util.Calendar
import javax.persistence.Column

@Embeddable
class Age (
    var birthDate: LocalDateTime,
    @Column(name = "age")
    var value: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Age

        if (birthDate != other.birthDate) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = birthDate.hashCode()
        result = 31 * result + value
        return result
    }

    companion object {
        fun generate(birthDate: LocalDateTime): Age {
            var currentAge = getAge(birthDate)

            return Age(birthDate, currentAge)
        }

        fun getAge(birthDate: LocalDateTime): Int {
            var currentYear = Calendar.getInstance().get(Calendar.YEAR)
            var birthYear = birthDate.year

            return currentYear - birthYear + 1
        }
    }
}