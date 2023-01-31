package basic.orderapi.order.command.application

import basic.orderapi.common.model.Address
import basic.orderapi.order.command.domain.entity.Order
import basic.orderapi.order.command.domain.value.OrderLine
import basic.orderapi.order.command.domain.value.OrderState
import basic.orderapi.order.command.domain.value.Orderer

data class OrderDto (
    var orderNumber: String,
    var orderLines: List<OrderLine>,
    var totalAmounts: Int,
    var orderState: OrderState,
    var shippingInfo: Address,
    var orderer: Orderer
) {
    companion object {
        fun ofOrder(order: Order): OrderDto {
            return OrderDto(order.orderNumber!!.number, order.orderLines, order.totalAmounts.value,
                order.orderState, order.shippingInfo, order.orderer)
        }
    }
}