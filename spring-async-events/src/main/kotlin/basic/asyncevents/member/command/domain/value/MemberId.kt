package basic.asyncevents.member.command.domain.value

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class MemberId (
    @Column(name = "member_id")
    var id: String? = null
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberId

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        fun generate(): MemberId {
            val randomNum = UUID.randomUUID().toString()
            return MemberId(randomNum)
        }
    }
}