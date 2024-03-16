package hr.jedvaj.demo.obms.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encoder: PasswordEncoder = BCryptPasswordEncoder()
        return encoder
    }

    @Bean
    fun usersManager(dataSource: DataSource): UserDetailsManager {
        return JdbcUserDetailsManager(dataSource)
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }
            .sessionManagement { s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic(Customizer.withDefaults())
            .csrf { it.disable() }
            .build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring()
                .requestMatchers(AntPathRequestMatcher("/h2-console/**"))
        }
    }

}
