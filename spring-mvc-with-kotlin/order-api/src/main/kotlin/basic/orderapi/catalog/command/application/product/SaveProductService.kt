package basic.orderapi.catalog.command.application.product

import basic.orderapi.catalog.command.application.NotFoundCategoryException
import basic.orderapi.catalog.command.domain.category.repository.CategoryRepository
import basic.orderapi.catalog.command.domain.category.value.CategoryId
import basic.orderapi.catalog.command.domain.product.entity.Product
import basic.orderapi.catalog.command.domain.product.repository.ProductRepository
import basic.orderapi.catalog.command.domain.product.value.ProductId
import org.springframework.stereotype.Service

@Service
class SaveProductService (
    val productRepository: ProductRepository,
    val categoryRepository: CategoryRepository
) {
    fun save(productRequest: ProductRequest): ProductId {
        validateCategoryId(productRequest.categoryIDs)
        var productId = ProductId.generate()

        var product = Product(productId, productRequest.categoryIDs, productRequest.price,
            productRequest.name, productRequest.detail)
        productRepository.save(product)
        return productId
    }

    private fun validateCategoryId(categoryIds: Set<CategoryId>) {
        for(categoryId in categoryIds) {
            categoryRepository.findById(categoryId).orElseThrow { throw NotFoundCategoryException("Not Found Category") }
        }
    }
}