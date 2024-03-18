package hr.jedvaj.demo.obms.model.request

import jakarta.validation.constraints.Pattern

data class UserUpdateRequest(

    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6}\$")
    var password: String?,

    var enabled: Boolean?
){
}