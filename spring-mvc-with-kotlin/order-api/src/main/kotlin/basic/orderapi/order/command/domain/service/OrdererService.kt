package basic.orderapi.order.command.domain.service

import basic.orderapi.member.command.domain.value.MemberId
import basic.orderapi.order.command.domain.value.Orderer

interface OrdererService {
    fun createOrderer(memberId: MemberId): Orderer
}