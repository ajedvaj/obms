package hr.jedvaj.demo.obms.model.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "book_catalogue")
data class Book(

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val author: String,

    @Column
    val genre: String? = null,

    @Column
    val price: BigDecimal? = null,

    @Column(nullable = false)
    val availability: Boolean

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    var modifiedAt: LocalDateTime? = null

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: String? = null

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    var modifiedBy: String? = null

}
