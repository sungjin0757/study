package basic.orderapi.order.command.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory

class OrderRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): OrderRepositoryCustom {
}