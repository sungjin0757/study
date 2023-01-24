package basic.orderapi.catalog.command.domain.product.repository

import basic.orderapi.catalog.command.domain.product.entity.Product
import basic.orderapi.catalog.command.domain.product.value.ProductId
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface ProductRepository: CrudRepository<Product, ProductId>, ProductRepositoryCustom,
    QuerydslPredicateExecutor<Product> {
}