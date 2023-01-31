package basic.orderapi.catalog.command.application

class NotFoundCategoryException (
    var reason: String
): RuntimeException(reason) {
}