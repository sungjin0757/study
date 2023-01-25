package basic.orderapi.order.command.application

import basic.orderapi.catalog.command.domain.product.entity.Product
import basic.orderapi.catalog.command.domain.product.repository.ProductRepository
import basic.orderapi.catalog.command.domain.product.value.ProductId
import basic.orderapi.order.command.domain.entity.Order
import basic.orderapi.order.command.domain.repository.OrderRepository
import basic.orderapi.order.command.domain.service.OrdererService
import basic.orderapi.order.command.domain.value.OrderLine
import basic.orderapi.order.command.domain.value.OrderNo
import basic.orderapi.order.command.domain.value.OrderState
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList

@Service
class PlaceOrderService (
    val ordererService: OrdererService,
    val orderRepository: OrderRepository,
    val productRepository: ProductRepository
) {
    @Transactional
    fun placeOrder(orderRequest: OrderRequest): OrderNo {
        var orderLines: MutableList<OrderLine> = ArrayList<OrderLine>()
        for(orderProduct in orderRequest.orderProducts) {
            var productOpt: Optional<Product> = productRepository.findById(ProductId(orderProduct.productId))
            var product = productOpt.orElseThrow {throw NoOrderProductException(orderProduct.productId)}
            orderLines.add(OrderLine(product.id!!, product.price, orderProduct.quantity))
        }
        var orderNo = OrderNo.generate()
        var orderer = ordererService.createOrderer(orderRequest.ordererMemberId)

        var order = Order(orderNo, orderLines, orderRequest.shippingInfo, OrderState.PAYMENT_WAITING, orderer)
        orderRepository.save(order)
        return orderNo
    }
}