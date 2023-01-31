package basic.orderapi.order.command.domain.entity

import basic.orderapi.common.jpa.MoneyConverter
import basic.orderapi.common.model.AbstractTimeEntity
import basic.orderapi.common.model.Address
import basic.orderapi.common.model.Money
import basic.orderapi.order.command.domain.value.OrderLine
import basic.orderapi.order.command.domain.value.OrderNo
import basic.orderapi.order.command.domain.value.OrderState
import basic.orderapi.order.command.domain.value.Orderer
import javax.persistence.*

@Entity
@Table(name = "purchase_order")
@Access(AccessType.FIELD)
class Order (
    @EmbeddedId
    var orderNumber: OrderNo?,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_line", joinColumns = [JoinColumn(name = "order_number")])
    var orderLines: MutableList<OrderLine>,

    @Convert(converter = MoneyConverter::class)
    var totalAmounts: Money,

    @Enumerated(EnumType.STRING)
    var orderState: OrderState,

    @Embedded
    var shippingInfo: Address,

    @Embedded
    var orderer: Orderer
): AbstractTimeEntity() {
    companion object {
          fun generate(orderNumber: OrderNo, orderLines: List<OrderLine>,
                       shippingInfo: Address, orderState: OrderState, orderer: Orderer): Order {
              verifyAtLeastOneOrMoreOrderLInes(orderLines)
              return Order(orderNumber, orderLines.toMutableList(), calculateTotalAmounts(orderLines),
                  orderState, shippingInfo, orderer)
          }
        private fun verifyAtLeastOneOrMoreOrderLInes(orderLines: List<OrderLine>) {
            if(orderLines.isNullOrEmpty())
                throw IllegalArgumentException("No OrderLine")
        }
        private fun calculateTotalAmounts(orderLines: List<OrderLine>): Money {
            return Money(orderLines.sumOf { it.amounts.value })
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        if (orderNumber != other.orderNumber) return false
        if (shippingInfo != other.shippingInfo) return false
        if (orderLines != other.orderLines) return false
        if (totalAmounts != other.totalAmounts) return false
        if (orderState != other.orderState) return false

        return true
    }

    override fun hashCode(): Int {
        var result = orderNumber?.hashCode() ?: 0
        result = 31 * result + shippingInfo.hashCode()
        result = 31 * result + orderLines.hashCode()
        result = 31 * result + totalAmounts.hashCode()
        result = 31 * result + orderState.hashCode()
        return result
    }
}