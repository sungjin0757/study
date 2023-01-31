package basic.orderapi.catalog.command.application.category

class DuplicateCategoryException(
    var reason: String
): RuntimeException(reason) {
}