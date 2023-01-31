package basic.orderapi.catalog.command.domain.category.repository

import basic.orderapi.catalog.command.domain.category.entity.Category
import basic.orderapi.catalog.command.domain.category.entity.QCategory
import basic.orderapi.catalog.command.domain.category.entity.QCategory.*
import com.querydsl.jpa.impl.JPAQueryFactory

class CategoryRepositoryImpl (
    val jpaQueryFactory: JPAQueryFactory
): CategoryRepositoryCustom {
    override fun searchByName(name: String): Category? {
        return jpaQueryFactory
            .selectFrom(category)
            .where(category.name.eq(name))
            .fetchOne()
    }
}