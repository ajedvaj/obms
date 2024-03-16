package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.config.SecurityRoles
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
    val passwordEncoder: PasswordEncoder
) {

    fun getAll(): List<UserResponse> {
        val users = userRepository.findAll()
        return userMapper.toDtoList(users)
    }

    fun getOne(id: Long): UserResponse? {
        val user = userRepository.findByIdOrNull(id)
        return userMapper.toDto(user)
    }

    fun create(userCreateRequest: UserCreateRequest): UserResponse {
        // create user with SpringSecurity UserDetailsManager
        val authorities = listOf(SimpleGrantedAuthority(SecurityRoles.ROLE_USER.name))
        val password = passwordEncoder.encode(userCreateRequest.password.trim())
        val user =
            org.springframework.security.core.userdetails.User(userCreateRequest.username, password, authorities)
        userDetailsManager.createUser(user)

        // load it with JPA
        val entity = userRepository.findByUsername(user.username)
        return userMapper.toDto(entity)!!
    }

    fun update(id: Long, userReq: UserUpdateRequest) {
        val user = userRepository.findByIdOrNull(id)!!
        user.password = passwordEncoder.encode(userReq.password.trim())
        userRepository.save(user)
    }

    fun delete(id: Long) {
        val user = userRepository.findByIdOrNull(id)
        userDetailsManager.deleteUser(user?.username)
    }
}