package basic.orderapi.catalog.command.domain.category.repository

import basic.orderapi.catalog.command.domain.category.entity.Category

interface CategoryRepositoryCustom {
    fun searchByName(name: String): Category?
}