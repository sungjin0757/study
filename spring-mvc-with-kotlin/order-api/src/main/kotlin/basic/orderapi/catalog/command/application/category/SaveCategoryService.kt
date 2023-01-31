package basic.orderapi.catalog.command.application.category

import basic.orderapi.catalog.command.domain.category.entity.Category
import basic.orderapi.catalog.command.domain.category.repository.CategoryRepository
import basic.orderapi.catalog.command.domain.category.value.CategoryId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SaveCategoryService (
    val categoryRepository: CategoryRepository
) {
    @Transactional
    fun save(categoryRequest: CategoryRequest): CategoryId {
        validateDuplicateCategory(categoryRequest.name)

        var categoryId = CategoryId.generate()

        var category = Category(categoryId, categoryRequest.name)
        categoryRepository.save(category)
        return categoryId
    }

    private fun validateDuplicateCategory(name: String) {
        if(categoryRepository.searchByName(name) !== null)
            throw DuplicateCategoryException("$name is Already Exists")
    }
}