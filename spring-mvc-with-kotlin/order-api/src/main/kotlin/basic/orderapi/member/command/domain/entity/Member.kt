package basic.orderapi.member.command.domain.entity

import basic.orderapi.common.model.AbstractTimeEntity
import basic.orderapi.common.model.Address
import basic.orderapi.member.command.domain.value.MemberId
import javax.persistence.*

@Entity
@Table(name = "member")
class Member (
    @EmbeddedId
    var id: MemberId,
    @Column(nullable = false)
    var name: String,
    @Embedded
    @Column(nullable = false)
    var address: Address
): AbstractTimeEntity() {

    fun changeAddress(newAddress: Address) {
        this.address = address
    }

    fun changeName(newName: String) {
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (name != other.name) return false
        if (address != other.address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + address.hashCode()
        return result
    }

}