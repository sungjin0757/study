package basic.orderapi.order.command.application

import basic.orderapi.common.model.Address
import basic.orderapi.member.command.domain.value.MemberId

class OrderRequest (
    var orderProducts: List<OrderProduct>,
    var ordererMemberId: MemberId,
    var shippingInfo: Address
) {
}