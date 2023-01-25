package basic.orderapi.order.command.application

class NoOrderProductException(
    var productId: String
): RuntimeException() {

}