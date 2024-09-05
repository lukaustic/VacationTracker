package vacationsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VacationSearchApplication

fun main(args: Array<String>) {
	runApplication<VacationSearchApplication>(*args)
}