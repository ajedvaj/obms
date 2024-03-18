package hr.jedvaj.demo.obms.controller

import hr.jedvaj.demo.obms.model.request.BookRequest
import hr.jedvaj.demo.obms.model.response.BookResponse
import hr.jedvaj.demo.obms.service.BookService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/books")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
class BookController (val bookService: BookService) {


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping
    fun getAll(): ResponseEntity<List<BookResponse>> {
        val books = bookService.getAll()
        return ResponseEntity.ok().body(books)
    }

    @PostMapping
    fun create(@Valid @RequestBody book: BookRequest): ResponseEntity<Void> {
        val bookResponse = bookService.create(book)
        return ResponseEntity.created(URI.create("/api/books/${bookResponse?.id}")).build()
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<BookResponse> {
        val book = bookService.getOne(id)
        book ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().body(book)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody bookRequest: BookRequest): ResponseEntity<Void> {
        val book = bookService.update(id, bookRequest)
        book ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        val success = bookService.delete(id)
        return if(success) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}