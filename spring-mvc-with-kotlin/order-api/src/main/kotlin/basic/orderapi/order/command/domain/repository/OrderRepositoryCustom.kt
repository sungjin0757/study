package basic.orderapi.order.command.domain.repository

import basic.orderapi.order.command.domain.entity.Order

interface OrderRepositoryCustom {
    fun searchAll(): List<Order>
}