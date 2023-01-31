package basic.orderapi.order.command.application

import basic.orderapi.common.model.Address
import basic.orderapi.order.command.domain.entity.Order
import basic.orderapi.order.command.domain.value.OrderLine
import basic.orderapi.order.command.domain.value.OrderState
import basic.orderapi.order.command.domain.value.Orderer

data class OrderDto (
    var orderNumber: String,
    var orderLineDtos: List<OrderLineDto>,
    var totalAmounts: Int,
    var orderState: OrderState,
    var shippingInfo: Address,
    var orderer: Orderer
) {
    companion object {
        fun ofOrder(order: Order): OrderDto {
            val orderLineDtos = order.orderLines.map { OrderLineDto.ofOrderLine(it) }
            return OrderDto(order.orderNumber!!.number, orderLineDtos, order.totalAmounts.value,
                order.orderState, order.shippingInfo, order.orderer)
        }
    }
}