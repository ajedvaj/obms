package hr.jedvaj.demo.obms.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(nullable = false)
        val username: String,

        @Column(nullable = false)
        var password: String,

        @Column(nullable = false)
        var enabled: Boolean = true
) {
        constructor(username: String, password: String) : this(null, username, password, true)
}