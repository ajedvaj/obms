package hr.jedvaj.demo.obms.repository

import hr.jedvaj.demo.obms.config.TestJpaAuditConfig
import hr.jedvaj.demo.obms.model.entity.Book
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal


@DataJpaTest
@Import(TestJpaAuditConfig::class)
class BookRepositoryTest(@Autowired val bookRepository: BookRepository) {


    @Test
    fun whenFindAll_thenReturnListOfRecord(){
        val books = bookRepository.findAll()

        assertNotNull(books)
        assertEquals(3, books.size)
    }

    @Test
    fun whenFindById_thenReturnRecord(){
        val book = bookRepository.findByIdOrNull(1L)

        assertNotNull(book)
        assertEquals(1, book?.id)
    }

    @Test
    fun whenSave_ThenReturnNewlyCreatedRecord(){
        val b1 = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)
        val entity = bookRepository.save(b1)

        assertNotNull(entity)
        assertNotNull(entity.createdAt)
        assertNotNull(entity.modifiedAt)
        assertEquals(entity.createdAt, entity.modifiedAt)
        assertEquals("Book1", entity.title)
        assertEquals("Author1", entity.author)
        assertEquals("Drama", entity.genre)
        assertEquals(BigDecimal.ONE, entity.price)
        assertEquals(true, entity.availability)
        assertEquals("test", entity.createdBy)
        assertEquals("test", entity.modifiedBy)
    }

    @Test
    fun whenUpdate_ThenReturnUpdatedRecord(){

        val book = Book("Book1", "Author1", "Drama", BigDecimal.ONE, true)
        val entity = bookRepository.save(book)

        val toUpdate = Book("Book2", "Author2", "Action", BigDecimal.TEN, false)
        toUpdate.id = entity.id
        toUpdate.createdAt = book.createdAt
        toUpdate.createdBy = book.createdBy

        bookRepository.save(toUpdate)
        bookRepository.flush()

        assertNotNull(entity)
        assertNotEquals(entity.createdAt, entity.modifiedAt)
        assertEquals("test", entity.modifiedBy)
        assertEquals("Book2", entity.title)
        assertEquals("Author2", entity.author)
        assertEquals("Action", entity.genre)
        assertEquals(BigDecimal.TEN, entity.price)
        assertEquals(false, entity.availability)
    }

    @Test
    fun whenDeleteById_thenReturnRecord(){
        bookRepository.deleteById(1L)

        val book = bookRepository.findByIdOrNull(1L)
        assertNull(book)
    }
}