package basic.asyncevents.member.command.domain.entity

import basic.asyncevents.common.event.Events
import basic.asyncevents.common.model.AbstractDateEntity
import basic.asyncevents.member.command.domain.value.MemberId
import basic.asyncevents.member.command.domain.value.MemberJoinedEvent
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "member")
class Member (
    @EmbeddedId
    var id: MemberId,

    @Column(name = "user_name", nullable = false)
    var name: String,

    @Column(name = "user_email", nullable = false)
    var email: String
): AbstractDateEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }

    companion object {
        fun of(id: MemberId, name: String, email: String): Member {
            Events.raise(MemberJoinedEvent(id, name, email))
            return Member(id, name, email)
        }
    }
}