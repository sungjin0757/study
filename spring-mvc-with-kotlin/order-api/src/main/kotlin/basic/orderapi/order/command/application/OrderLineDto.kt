package basic.orderapi.order.command.application

import basic.orderapi.order.command.domain.value.OrderLine

data class OrderLineDto (
    var productId: String,
    var price: Int,
    var quantity: Int,
    var amounts: Int
) {
    companion object {
        fun ofOrderLine(orderLine: OrderLine): OrderLineDto {
            return OrderLineDto(orderLine.productId!!.id, orderLine.price.value,
                orderLine.quantity, orderLine.amounts.value)
        }
    }
}