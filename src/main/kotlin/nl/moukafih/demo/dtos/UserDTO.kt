package nl.moukafih.demo.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import nl.moukafih.demo.entities.Role
import java.util.*

data class UserDTO(
    val id: String? = null,
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,
    val role: Role,
    @field:NotBlank(message = "First name is required")
    val firstname: String,
    val lastname: String,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
