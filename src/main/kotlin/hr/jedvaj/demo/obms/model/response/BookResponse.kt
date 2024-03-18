package hr.jedvaj.demo.obms.model.response

import java.math.BigDecimal

data class BookResponse(
    val id: Long,
    val title: String,
    val author: String,
    val genre: String?,
    val price: BigDecimal?,
    val availability: Boolean
)
