package hr.jedvaj.demo.obms.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class BookRequest(
    @field:NotBlank
    val title: String?,

    @field:NotBlank
    val author: String?,

    val genre: String?,

    val price: BigDecimal?,

    @field:NotNull
    val availability: Boolean?
)
