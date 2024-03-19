package hr.jedvaj.demo.obms.repository

import hr.jedvaj.demo.obms.model.entity.Authority
import hr.jedvaj.demo.obms.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository: JpaRepository<Authority, Long> {
}