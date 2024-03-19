package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.config.SecurityRoles
import hr.jedvaj.demo.obms.model.entity.Authority
import hr.jedvaj.demo.obms.model.entity.User
import hr.jedvaj.demo.obms.model.mapper.UserMapper
import hr.jedvaj.demo.obms.model.request.UserCreateRequest
import hr.jedvaj.demo.obms.model.request.UserUpdateRequest
import hr.jedvaj.demo.obms.model.response.UserResponse
import hr.jedvaj.demo.obms.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val userMapper: UserMapper,
    val userDetailsManager: UserDetailsManager,
    val passwordEncoder: PasswordEncoder,
    val authorityService: AuthorityService
) {

    fun getAll(): List<UserResponse> {
        val users = userRepository.findAll()
        return userMapper.toDtoList(users)
    }

    fun getOne(id: Long): UserResponse? {
        val user = userRepository.findByIdOrNull(id) ?: return null
        return userMapper.toDto(user)
    }

    fun create(userCreateRequest: UserCreateRequest): UserResponse? {

        val password = passwordEncoder.encode(userCreateRequest.password?.trim())
        val user = User(userCreateRequest.username, password)

        val entity = userRepository.save(user)
        val future = authorityService.createForUser(user.username)
        future.thenAccept {
            println("For demo purpose AuthorityService uses @Async")
        }

        return userMapper.toDto(entity)
    }

    fun update(id: Long, userReq: UserUpdateRequest): UserResponse? {
        val user = userRepository.findByIdOrNull(id) ?: return null

        userReq.password?.let { user.password = passwordEncoder.encode(userReq.password?.trim()) }
        userReq.enabled?.let { user.enabled = userReq.enabled!! }

        val result = userRepository.save(user)
        return userMapper.toDto(result)
    }

    fun delete(id: Long): Boolean {
        val user = userRepository.findByIdOrNull(id) ?: return false
        userDetailsManager.deleteUser(user.username)
        return true
    }
}