package hr.jedvaj.demo.obms.config

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@DataJpaTest
@EnableJpaAuditing(auditorAwareRef = "testAuditor")
class TestJpaAuditConfig {

    @Bean
    fun testAuditor(): AuditorAware<String> {
        return AuditorAware {
            Optional.ofNullable("test")
        }
    }
}