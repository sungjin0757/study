package basic.orderapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderApiApplication

fun main(args: Array<String>) {
	runApplication<OrderApiApplication>(*args)
}
