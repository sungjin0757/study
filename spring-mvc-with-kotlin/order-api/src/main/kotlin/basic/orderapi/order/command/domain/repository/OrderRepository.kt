package basic.orderapi.order.command.domain.repository

import basic.orderapi.order.command.domain.entity.Order
import basic.orderapi.order.command.domain.value.OrderNo
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import java.util.*
import java.util.concurrent.ThreadLocalRandom

interface OrderRepository: CrudRepository<Order, OrderNo>,
    OrderRepositoryCustom, QuerydslPredicateExecutor<Order> {

        fun  nextOrderNo(): OrderNo {
            var randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
            var number = String.format("%tY%<tm%<td%<tH-%d", Date(), randomNo)
            return OrderNo(number)
        }
}