package hr.jedvaj.demo.obms.repository

import hr.jedvaj.demo.obms.model.entity.Authority
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class AuthorityRepositoryTest(@Autowired val authorityRepository: AuthorityRepository) {

    @Test
    fun whenSave_thenSaveAndReturnRecord() {
        val authority = Authority("user", "ROLE_TEST")
        val entity = authorityRepository.save(authority)

        assertNotNull(entity)
        assertEquals(3L, entity.id)
    }
}