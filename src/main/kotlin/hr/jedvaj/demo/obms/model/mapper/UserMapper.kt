package hr.jedvaj.demo.obms.model.mapper

import hr.jedvaj.demo.obms.model.entity.User
import hr.jedvaj.demo.obms.model.response.UserResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class UserMapper {

    abstract fun toDto(user: User?): UserResponse?
    abstract fun toDtoList(userList: List<User>): List<UserResponse>

}