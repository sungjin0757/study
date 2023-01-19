package basic.orderapi.common.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Address(
    @Column(name = "zip_code")
    var zipCode: String,
    @Column(name = "address1")
    var address1: String,
    @Column(name = "address2")
    var address2: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (zipCode != other.zipCode) return false
        if (address1 != other.address1) return false
        if (address2 != other.address2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = zipCode.hashCode()
        result = 31 * result + address1.hashCode()
        result = 31 * result + address2.hashCode()
        return result
    }


}