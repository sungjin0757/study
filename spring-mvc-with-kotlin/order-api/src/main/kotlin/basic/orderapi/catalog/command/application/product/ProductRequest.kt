package basic.orderapi.catalog.command.application.product

import basic.orderapi.catalog.command.domain.category.value.CategoryId
import basic.orderapi.common.model.Money

class ProductRequest (
    var categoryIDs: MutableSet<CategoryId>,
    var price: Money,
    var name: String,
    var detail: String
) {
}