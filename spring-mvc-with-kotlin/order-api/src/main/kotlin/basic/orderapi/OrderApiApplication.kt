package basic.orderapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class OrderApiApplication

fun main(args: Array<String>) {
	runApplication<OrderApiApplication>(*args)
}
