package hr.jedvaj.demo.obms.repository

import hr.jedvaj.demo.obms.model.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {
}