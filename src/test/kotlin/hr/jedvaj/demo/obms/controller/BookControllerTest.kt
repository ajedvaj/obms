package hr.jedvaj.demo.obms.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hr.jedvaj.demo.obms.model.request.BookRequest
import hr.jedvaj.demo.obms.model.response.BookResponse
import hr.jedvaj.demo.obms.service.BookService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(controllers = [BookController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class BookControllerTest(@Autowired val mockMvc: MockMvc) {

    companion object {
        const val BASE_URL = "/api/books"
    }

    @MockkBean
    lateinit var bookService: BookService

    @Test
    fun whenGetAll_thenReturnListOfRecordsJsonWithStatus200() {
        val b1 = BookResponse(1, "Book1", "Author1", "Drama", BigDecimal.ONE, true)
        val b2 = BookResponse(2, "Book1", "Author1", "Drama", BigDecimal.ONE, true)

        every { bookService.getAll() } returns listOf(b1, b2)

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun givenExistingId_whenGet_thenReturnJsonWithStatus200() {
        val id = 1L
        val b1 = BookResponse(id, "Book1", "Author1", "Drama", BigDecimal.ONE, true)

        every { bookService.getOne(id) } returns b1

        mockMvc.perform(get("${BASE_URL}/${id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Book1"))
    }

    @Test
    fun givenNonExistingId_whenGet_thenReturnStatus404() {
        val id = 10L
        every { bookService.getOne(id) } returns null

        mockMvc.perform(get("${BASE_URL}/${id}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun whenCreate_thenCreateRecordAndReturnStatus200() {
        val id = 1L
        val bReq = BookRequest("Book1", "Author1", "Action", null, true)
        val b1 = BookResponse(id, "Book1", "Author1", "Action", null, true)
        val objectMapper = ObjectMapper()
        val jsonRequest = objectMapper.writeValueAsString(bReq)

        every { bookService.create(bReq) } returns b1

        mockMvc.perform(
            post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isCreated)
            .andExpect(header().stringValues("Location", "${BASE_URL}/1"))
    }

    @Test
    fun whenUpdate_thenUpdateRecordAndReturnStatus200() {
        val id = 1L
        val bReq = BookRequest("Book1", "Author1", "Action", null, true)
        val b1 = BookResponse(id, "Book1", "Author1", "Action", null, true)
        val objectMapper = ObjectMapper()
        val jsonRequest = objectMapper.writeValueAsString(bReq)

        every { bookService.update(id, bReq) } returns b1

        mockMvc.perform(
            put("${BASE_URL}/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk)
    }

    @Test
    fun givenExistingId_whenDelete_thenReturn200() {
        val id = 1L
        every { bookService.delete(id) } returns true

        mockMvc.perform(delete("${BASE_URL}/${id}"))
            .andExpect(status().isOk)
    }

    @Test
    fun givenNonExistingId_whenDelete_thenReturn404() {
        val id = 1L
        every { bookService.delete(id) } returns false

        mockMvc.perform(delete("${BASE_URL}/${id}"))
            .andExpect(status().isNotFound)
    }
}