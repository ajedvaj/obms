package hr.jedvaj.demo.obms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class OnlineBookstoreManagementSystemApplication

fun main(args: Array<String>) {
	runApplication<OnlineBookstoreManagementSystemApplication>(*args)
}
