package basic.orderapi.catalog.command.domain.category.repository

import com.querydsl.jpa.impl.JPAQueryFactory

class CategoryRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): CategoryRepositoryCustom {
}