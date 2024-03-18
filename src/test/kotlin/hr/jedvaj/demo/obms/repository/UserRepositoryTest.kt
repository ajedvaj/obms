package hr.jedvaj.demo.obms.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull


@DataJpaTest
class UserRepositoryTest(@Autowired val userRepository: UserRepository) {


    @Test
    fun whenFindAll_thenReturnListOfRecord(){
        val users = userRepository.findAll()

        assertNotNull(users)
        assertEquals(2, users.size)
    }

    @Test
    fun whenFindById_thenReturnRecord(){
        val user = userRepository.findByIdOrNull(1L)

        assertNotNull(user)
        assertEquals(1, user?.id)
        assertEquals("admin", user?.username)
    }

    @Test
    fun whenFindByUsername_thenReturnRecord(){
        val user = userRepository.findByUsername("admin")

        assertNotNull(user)
        assertEquals(1, user?.id)
        assertEquals("admin", user?.username)
    }

    @Test
    fun whenSave_thenReturnUpdatedRecord(){
        val user = userRepository.findByUsername("admin")
        user.password = "newPassword"

        userRepository.save(user)
        assertNotNull(user)
        assertEquals(1, user?.id)
        assertEquals("newPassword", user?.password)
    }


}