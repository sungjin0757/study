package basic.pessimisticlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class PessimisticLockApplication

fun main(args: Array<String>) {
	runApplication<PessimisticLockApplication>(*args)
}
