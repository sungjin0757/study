package basic.orderapi.catalog.command.domain.product.entity

import basic.orderapi.catalog.command.domain.category.value.CategoryId
import basic.orderapi.catalog.command.domain.product.value.ProductId
import basic.orderapi.common.jpa.MoneyConverter
import basic.orderapi.common.model.Money
import javax.persistence.*

@Entity
@Table(name = "product")
class Product (
    @EmbeddedId
    var id: ProductId?,
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_category", joinColumns = [JoinColumn(name = "product_id")])
    var categoryIds: MutableSet<CategoryId>,
    @Convert(converter = MoneyConverter::class)
    var price: Money,
    var name: String,
    var detail: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (categoryIds != other.categoryIds) return false
        if (price != other.price) return false
        if (name != other.name) return false
        if (detail != other.detail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + categoryIds.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + detail.hashCode()
        return result
    }
}