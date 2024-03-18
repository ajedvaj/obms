package hr.jedvaj.demo.obms.model.mapper

import hr.jedvaj.demo.obms.model.entity.Book
import hr.jedvaj.demo.obms.model.request.BookRequest
import hr.jedvaj.demo.obms.model.response.BookResponse
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(componentModel = "spring")
interface BookMapper {

    fun toDto(book: Book?): BookResponse?
    fun toDtoList(bookList: List<Book>): List<BookResponse>
    fun toEntity(book: BookRequest): Book
}