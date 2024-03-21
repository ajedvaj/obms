package hr.jedvaj.demo.obms.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class EmailService {

    val logger = KotlinLogging.logger {}

    @Async
    fun sendEmail(username: String): CompletableFuture<Boolean> {
        logger.info { "This is me pretending to send email for @Async example" }
        return CompletableFuture.completedFuture(true)
    }
}