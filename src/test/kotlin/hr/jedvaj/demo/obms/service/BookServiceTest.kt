package hr.jedvaj.demo.obms.service

import hr.jedvaj.demo.obms.model.entity.Book
import hr.jedvaj.demo.obms.model.mapper.BookMapper
import hr.jedvaj.demo.obms.model.mapper.BookMapperImpl
import hr.jedvaj.demo.obms.model.request.BookRequest
import hr.jedvaj.demo.obms.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal

class BookServiceTest {

    val bookRepository: BookRepository = mockk()
    val bookMapper: BookMapper = BookMapperImpl()
    val bookService: BookService = BookService(bookRepository, bookMapper)

    @Test
    fun whenGetAll_thenReturnListOfRecords() {
        val b1 = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)
        val b2 = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)

        every { bookRepository.findAll() } returns listOf(b1, b2)
        val result = bookService.getAll()

        verify(exactly = 1) { bookRepository.findAll() }
        assertEquals(2, result.size)
    }

    @Test
    fun whenGetOne_thenReturnOneRecord() {
        val id = 1L
        val b1 = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)

        every { bookRepository.findByIdOrNull(id) } returns b1
        val result = bookService.getOne(id)

        verify(exactly = 1) { bookRepository.findByIdOrNull(id) }
        assertEquals("Book1", result?.title)
        assertEquals("Author1", result?.author)
        assertEquals("Drama", result?.genre)
        assertEquals(BigDecimal.ONE, result?.price)
        assertEquals(true, result?.availability)
    }

    @Test
    fun whenCreate_thenSaveRecordAndReturnRecord() {
        val bookRequest = BookRequest("Book1", "Author1", "Drama", BigDecimal.ONE, true)
        val b1 = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)

        every { bookRepository.save(b1) } returns b1
        val result = bookService.create(bookRequest)

        verify(exactly = 1) { bookRepository.save(b1) }
        assertEquals(bookRequest.title, result?.title)
        assertEquals(bookRequest.author, result?.author)
        assertEquals(bookRequest.genre, result?.genre)
        assertEquals(bookRequest.price, result?.price)
        assertEquals(bookRequest.availability, result?.availability)
    }

    @Test
    fun whenUpdate_thenUpdateRecordAndReturnRecord() {
        val bookRequest = BookRequest("Book1Update", "Author1", "Drama", BigDecimal.ONE, true)
        val b1 = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)
        val b2 = Book("Book1Update", "Author1", "Drama", BigDecimal.ONE, true)
        val id = 1L

        every { bookRepository.findByIdOrNull(id) } returns b1
        every { bookRepository.save(b2) } returns b2
        val result = bookService.update(id, bookRequest)

        verify(exactly = 1) { bookRepository.save(b2) }
        assertEquals(bookRequest.title, result?.title)
        assertEquals(bookRequest.author, result?.author)
        assertEquals(bookRequest.genre, result?.genre)
        assertEquals(bookRequest.price, result?.price)
        assertEquals(bookRequest.availability, result?.availability)
    }

    @Test
    fun whenDelete_thenCallRepositoryToDeleteRecord() {
        val id = 1L
        every { bookRepository.existsById(id) } returns true
        every { bookRepository.deleteById(id) } returns Unit

        val result = bookService.delete(id)

        verify(exactly = 1) { bookRepository.deleteById(id) }
        assertTrue(result)
    }
}