package hr.jedvaj.demo.obms.controller

import hr.jedvaj.demo.obms.model.request.UserCreateRequest
import hr.jedvaj.demo.obms.model.request.UserUpdateRequest
import hr.jedvaj.demo.obms.model.response.UserResponse
import hr.jedvaj.demo.obms.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
class UsersController (val userService: UserService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAll()
        return ResponseEntity.ok().body(users)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @PostMapping
    fun create(@RequestBody user: UserCreateRequest): ResponseEntity<Void> {
        val userResponse = userService.create(user)
        return ResponseEntity.created(URI.create("/api/users/${userResponse.id}")).build()
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.getOne(id)
        user ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().body(user)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody userRequest: UserUpdateRequest): ResponseEntity<Void> {
        userService.update(id, userRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return  ResponseEntity.ok().build()
    }

}