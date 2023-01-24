package basic.orderapi.catalog.command.domain.category.repository

import basic.orderapi.catalog.command.domain.category.entity.Category
import basic.orderapi.catalog.command.domain.category.value.CategoryId
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface CategoryRepository: CrudRepository<Category, CategoryId>, CategoryRepositoryCustom,
    QuerydslPredicateExecutor<Category> {
}