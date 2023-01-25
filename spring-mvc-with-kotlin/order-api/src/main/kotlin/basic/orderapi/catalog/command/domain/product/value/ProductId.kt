package basic.orderapi.catalog.command.domain.product.value

import java.io.Serializable
import java.util.UUID
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
@Access(AccessType.FIELD)
class ProductId(
    @Column(name = "product_id")
    var id: String
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductId

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        fun generate(): ProductId {
            var randomNo = UUID.randomUUID().toString()
            return ProductId(randomNo)
        }
    }
}