package basic.orderapi.catalog.command.domain.product.repository

import com.querydsl.jpa.impl.JPAQueryFactory

class ProductRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): ProductRepositoryCustom {
}