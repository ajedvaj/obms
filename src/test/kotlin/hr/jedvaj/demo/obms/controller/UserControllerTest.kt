package hr.jedvaj.demo.obms.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import hr.jedvaj.demo.obms.model.request.UserCreateRequest
import hr.jedvaj.demo.obms.model.request.UserUpdateRequest
import hr.jedvaj.demo.obms.model.response.UserResponse
import hr.jedvaj.demo.obms.service.UserService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [UserController::class], excludeAutoConfiguration = [SecurityAutoConfiguration::class])
class UserControllerTest(@Autowired val mockMvc: MockMvc) {

    companion object {
        const val BASE_URL = "/api/users"
    }

    @MockkBean
    lateinit var userService: UserService

    @Test
    fun whenGetAll_thenReturnListOfRecordsJsonWithStatus200() {
        val u1 = UserResponse(1, "admin", true)
        val u2 = UserResponse(1, "user", true)

        every { userService.getAll() } returns listOf(u1, u2)

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
    }

    @Test
    fun givenExistingId_whenGet_thenReturnJsonWithStatus200() {
        val id = 1L
        val u1 = UserResponse(id, "admin", true)

        every { userService.getOne(id) } returns u1

        mockMvc.perform(MockMvcRequestBuilders.get("${BASE_URL}/${id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
    }

    @Test
    fun givenNonExistingId_whenGet_thenReturnStatus404() {
        val id = 10L
        every { userService.getOne(id) } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("${BASE_URL}/${id}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun givenValidRequest_whenCreate_thenCreateRecordAndReturnStatus200() {
        val id = 1L
        val userReq = UserCreateRequest("test", "test11")
        val userResp = UserResponse(id, "test", true)
        val objectMapper = ObjectMapper()
        val jsonRequest = objectMapper.writeValueAsString(userReq)

        every { userService.create(userReq) } returns userResp

        mockMvc.perform(
            MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.header().stringValues("Location", "${BASE_URL}/1"))
    }

    @Test
    fun givenValidRequest_whenUpdate_thenUpdateRecordAndReturnStatus200() {
        val id = 1L
        val userReq = UserUpdateRequest("test11", null)
        val userResp = UserResponse(id, "test", true)
        val objectMapper = ObjectMapper()
        val jsonRequest = objectMapper.writeValueAsString(userReq)

        every { userService.update(id, userReq) } returns userResp

        mockMvc.perform(
            MockMvcRequestBuilders.put("${BASE_URL}/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun givenExistingId_whenDelete_thenReturn200() {
        val id = 1L
        every { userService.delete(id) } returns true

        mockMvc.perform(MockMvcRequestBuilders.delete("${BASE_URL}/${id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun givenNonExistingId_whenDelete_thenReturn404() {
        val id = 1L
        every { userService.delete(id) } returns false

        mockMvc.perform(MockMvcRequestBuilders.delete("${BASE_URL}/${id}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

}