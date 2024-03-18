package hr.jedvaj.demo.obms.controller

import hr.jedvaj.demo.obms.model.request.UserCreateRequest
import hr.jedvaj.demo.obms.model.request.UserUpdateRequest
import hr.jedvaj.demo.obms.model.response.UserResponse
import hr.jedvaj.demo.obms.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
class UserController(val userService: UserService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAll()
        return ResponseEntity.ok().body(users)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @PostMapping
    fun create(@Valid @RequestBody user: UserCreateRequest): ResponseEntity<Void> {
        val userResponse = userService.create(user)
        return ResponseEntity.created(URI.create("/api/users/${userResponse?.id}")).build()
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.getOne(id)
        user ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().body(user)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody userRequest: UserUpdateRequest): ResponseEntity<Void> {
        val user = userService.update(id, userRequest)
        user ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        val success = userService.delete(id)
        return if(success) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

}