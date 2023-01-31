package basic.orderapi.order.command.application

import basic.orderapi.order.command.domain.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderDetailService (
    val orderRepository: OrderRepository
) {
    @Transactional
    fun getAllOrderList(): List<OrderDto> {
        val orders = orderRepository.searchAll()
        var orderDtos = orders.map { OrderDto.ofOrder(it) }

        return orderDtos
    }
}