package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.model.entity.Authority
import hr.jedvaj.demo.obms.repository.AuthorityRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class AuthorityServiceTest {

    val authorityRepository: AuthorityRepository = mockk()
    val authorityService: AuthorityService = AuthorityService(authorityRepository)

    @Test
    fun whenCreateForUser_thenSaveAndReturnRecord(){
        val user = "user"
        val authority = Authority(user, "ROLE_USER")
        every { authorityRepository.save(authority) } returns authority

        val entity = authorityService.createForUser(user)

        assertNotNull(entity)
        assertEquals("user", entity.username)
        assertEquals("ROLE_USER", entity.authority)

    }

}