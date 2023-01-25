package basic.orderapi.catalog.command.domain.category.value

import basic.orderapi.catalog.command.domain.product.value.ProductId
import java.io.Serializable
import java.util.*
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
@Access(AccessType.FIELD)
class CategoryId (
    @Column(name = "category_id")
    var value: String
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {
        fun generate(): CategoryId {
            var randomNo = UUID.randomUUID().toString()
            return CategoryId(randomNo)
        }
    }
}