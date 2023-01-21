package basic.orderapi.order.command.domain.value

import basic.orderapi.member.command.domain.value.MemberId
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Orderer (
    @AttributeOverrides(
        AttributeOverride(name = "id", column = Column(name = "orderer_id"))
    )
    var memberId: MemberId,
    @Column(name = "orderer_name")
    var name: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Orderer

        if (memberId != other.memberId) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}