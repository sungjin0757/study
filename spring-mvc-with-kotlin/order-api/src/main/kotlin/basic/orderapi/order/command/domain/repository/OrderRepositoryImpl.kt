package basic.orderapi.order.command.domain.repository

import basic.orderapi.order.command.domain.entity.Order
import basic.orderapi.order.command.domain.entity.QOrder
import basic.orderapi.order.command.domain.entity.QOrder.*
import com.querydsl.jpa.impl.JPAQueryFactory

class OrderRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): OrderRepositoryCustom {
    override fun searchAll(): List<Order> {
        return jpaQueryFactory
            .selectFrom(order)
            .fetch()
    }
}