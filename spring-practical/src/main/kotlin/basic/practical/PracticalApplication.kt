package basic.practical

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PracticalApplication

fun main(args: Array<String>) {
	runApplication<PracticalApplication>(*args)
}
