package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.model.entity.Authority
import hr.jedvaj.demo.obms.model.entity.User
import hr.jedvaj.demo.obms.model.mapper.UserMapper
import hr.jedvaj.demo.obms.model.mapper.UserMapperImpl
import hr.jedvaj.demo.obms.model.request.UserCreateRequest
import hr.jedvaj.demo.obms.model.request.UserUpdateRequest
import hr.jedvaj.demo.obms.model.response.UserResponse
import hr.jedvaj.demo.obms.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import java.util.concurrent.CompletableFuture

class UserServiceTest {

    val userRepository: UserRepository = mockk()
    val userDetailsManager: UserDetailsManager = mockk()
    val authorityService: AuthorityService = mockk()
    val passwordEncoder: PasswordEncoder = mockk()
    val userMapper: UserMapper = UserMapperImpl()
    val userService: UserService = UserService(userRepository, userMapper, userDetailsManager, passwordEncoder, authorityService)

    @Test
    fun whenGetAll_thenReturnListOfRecords() {
        val u1 = User(1, "user1", "user1", true )
        val u2 = User(2, "user2", "user2", true )

        every { userRepository.findAll() } returns listOf(u1, u2)
        val result = userService.getAll()

        verify(exactly = 1) { userRepository.findAll() }
        assertEquals(2, result.size)
    }

    @Test
    fun whenGetOne_thenReturnOneRecord() {
        val id = 1L
        val u1 = User(1, "user1", "user1", true )

        every { userRepository.findByIdOrNull(id) } returns u1
        val result = userService.getOne(id)

        verify(exactly = 1) { userRepository.findByIdOrNull(id) }
        assertEquals("user1", result?.username)
    }

    @Test
    fun whenCreate_thenSaveRecordAndReturnRecord() {
        val userReq = UserCreateRequest("test", "test")
        val entity = User("test", "test")
        val authority = Authority("test", "ROLE_USER")

        every { passwordEncoder.encode("test") } returns "test"
        every { userRepository.save(entity) } returns entity
        every { authorityService.createForUser("test") } returns CompletableFuture.completedFuture(authority)

        val created = userService.create(userReq)

        assertNotNull(created)
        assertEquals("test", created?.username)

    }

    @Test
    fun whenUpdate_thenUpdateRecordAndReturnRecord() {
        val id = 1L
        val userReq = UserUpdateRequest("test", null)
        val u1 = User(id, "user1", "user1", true )
        val u2 = User(id, "user1", "test", false )


        every { passwordEncoder.encode("test") } returns "test"
        every { userRepository.findByIdOrNull(id) } returns u1
        every { userRepository.save(u1) } returns u2
        val updated = userService.update(id, userReq)

        verify(exactly = 1) { userRepository.save(u1) }
        assertEquals(updated?.enabled, false)
    }

    @Test
    fun whenDelete_thenCallRepositoryToDeleteRecord() {
        val id = 1L
        val user = User("test", "test")
        every { userRepository.findByIdOrNull(id) } returns user
        every { userDetailsManager.deleteUser("test") } returns Unit

        val result = userService.delete(id)

        verify(exactly = 1) { userDetailsManager.deleteUser("test") }
        assertTrue(result)

    }
}