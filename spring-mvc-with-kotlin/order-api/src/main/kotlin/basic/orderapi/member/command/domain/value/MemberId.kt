package basic.orderapi.member.command.domain.value

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class MemberId(
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
        return id.hashCode()
    }

    companion object {
        fun of(id: String): MemberId {
            return MemberId(id)
        }
    }
}