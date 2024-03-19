package hr.jedvaj.demo.obms.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "authorities")
data class Authority(

        @Column(nullable = false)
        val username: String,

        @Column(nullable = false)
        var authority: String,

) {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null
}