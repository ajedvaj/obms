package hr.jedvaj.demo.obms.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import java.util.*


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaAuditConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {
            Optional.ofNullable(SecurityContextHolder.getContext())
                .map { obj -> obj.authentication }
                .filter { obj -> obj.isAuthenticated }
                .map { obj -> obj.principal }
                .map { obj -> User::class.java.cast(obj) }
                .map { obj -> obj.username }
        }
    }
}