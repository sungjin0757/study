package basic.orderapi.order.command.domain.value

import basic.orderapi.catalog.command.domain.product.value.ProductId
import basic.orderapi.common.jpa.MoneyConverter
import basic.orderapi.common.model.Money
import javax.persistence.Convert
import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
class OrderLine (
    @Embedded
    var productId: ProductId,
    @Convert(converter = MoneyConverter::class)
    var price: Money,
    var quantity: Int,
) {
    @Convert(converter = MoneyConverter::class)
    private var amounts: Money = calculateAmounts()
        get() {
            return amounts
        }

    private fun calculateAmounts(): Money {
        return price.multiply(quantity)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderLine

        if (price != other.price) return false
        if (quantity != other.quantity) return false
        if (amounts != other.amounts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = price.hashCode()
        result = 31 * result + quantity
        result = 31 * result + amounts.hashCode()
        return result
    }


}
