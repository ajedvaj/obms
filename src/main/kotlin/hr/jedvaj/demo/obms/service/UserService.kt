package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.model.entity.User
import hr.jedvaj.demo.obms.model.mapper.UserMapper
import hr.jedvaj.demo.obms.model.request.UserCreateRequest
import hr.jedvaj.demo.obms.model.request.UserUpdateRequest
import hr.jedvaj.demo.obms.model.response.UserResponse
import hr.jedvaj.demo.obms.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val userMapper: UserMapper,
    val userDetailsManager: UserDetailsManager,
    val passwordEncoder: PasswordEncoder,
    val authorityService: AuthorityService,
    val emailService: EmailService
) {
    val logger = KotlinLogging.logger {}

    fun getAll(): List<UserResponse> {
        logger.debug { "Received call to ${UserService::class.simpleName}.getAll()" }

        val users = userRepository.findAll()
        return userMapper.toDtoList(users)
    }

    fun getOne(id: Long): UserResponse? {
        logger.debug { "Received call to ${UserService::class.simpleName}.getOne() with path param $id" }

        val user = userRepository.findByIdOrNull(id) ?: return null
        return userMapper.toDto(user)
    }

    fun create(userCreateRequest: UserCreateRequest): UserResponse? {
        logger.debug { "Received call to ${UserService::class.simpleName}.create()" }

        val password = passwordEncoder.encode(userCreateRequest.password?.trim())
        val user = User(userCreateRequest.username, password)

        val entity = userRepository.save(user)
        authorityService.createForUser(user.username)

        val future = emailService.sendEmail(entity.username)
        future.thenAccept {
            logger.info { "For demo purpose example of processing async result" }
        }

        logger.info { "User creation done" }
        return userMapper.toDto(entity)
    }

    fun update(id: Long, userReq: UserUpdateRequest): UserResponse? {
        logger.debug { "Received call to ${UserService::class.simpleName}.update() with path param: $id" }

        val user = userRepository.findByIdOrNull(id) ?: return null

        userReq.password?.let { user.password = passwordEncoder.encode(userReq.password?.trim()) }
        userReq.enabled?.let { user.enabled = userReq.enabled!! }

        val result = userRepository.save(user)
        return userMapper.toDto(result)
    }

    fun delete(id: Long): Boolean {
        logger.debug { "Received call to ${UserService::class.simpleName}.delete() with path param: $id" }

        val user = userRepository.findByIdOrNull(id) ?: return false
        userDetailsManager.deleteUser(user.username)
        return true
    }
}