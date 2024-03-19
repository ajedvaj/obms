package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.model.mapper.BookMapper
import hr.jedvaj.demo.obms.model.request.BookRequest
import hr.jedvaj.demo.obms.model.response.BookResponse
import hr.jedvaj.demo.obms.repository.BookRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookService(val bookRepository: BookRepository, val bookMapper: BookMapper) {

    val logger = KotlinLogging.logger {}

    fun getAll(): List<BookResponse> {
        logger.debug { "Received call to ${BookService::class.simpleName}.getAll()" }
        val books = bookRepository.findAll()
        return bookMapper.toDtoList(books)
    }

    fun getOne(id: Long): BookResponse? {
        logger.debug { "Received call to ${BookService::class.simpleName}.getOne() with param id: $id" }
        val book = bookRepository.findByIdOrNull(id) ?: return null
        return bookMapper.toDto(book)
    }

    fun create(bookReq: BookRequest): BookResponse? {
        logger.debug { "Received call to ${BookService::class.simpleName}.create() with param bookReq: $bookReq" }
        val book = bookMapper.toEntity(bookReq)
        val entity = bookRepository.save(book)
        return bookMapper.toDto(entity)
    }

    fun update(id: Long, bookReq: BookRequest): BookResponse? {
        logger.debug { "Received call to ${BookService::class.simpleName}.update() with params id: $id , bookReq: $bookReq" }
        val entity = bookRepository.findByIdOrNull(id) ?: return null

        val updatedEntity = bookMapper.toEntity(bookReq)
        updatedEntity.id = entity.id

        val result = bookRepository.save(updatedEntity)
        return bookMapper.toDto(result)
    }

    fun delete(id: Long): Boolean {
        logger.debug { "Received call to ${BookService::class.simpleName}.delete() with param id: $id" }
        val exists = bookRepository.existsById(id)
        if(!exists) {
            return false
        }

        bookRepository.deleteById(id)
        return true
    }
}