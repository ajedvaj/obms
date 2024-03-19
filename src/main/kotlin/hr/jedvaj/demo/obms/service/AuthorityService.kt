package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.config.SecurityRoles
import hr.jedvaj.demo.obms.model.entity.Authority
import hr.jedvaj.demo.obms.repository.AuthorityRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class AuthorityService(val authorityRepository: AuthorityRepository) {

    @Async
    fun createForUser(username: String): CompletableFuture<Authority> {
        val authority = Authority(username, SecurityRoles.ROLE_USER.name)
        val entity = authorityRepository.save(authority)
        return CompletableFuture.completedFuture(entity)
    }
}