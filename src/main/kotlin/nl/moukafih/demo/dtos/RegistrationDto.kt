package nl.moukafih.demo.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegistrationDto(
    val id: String? = null,
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,
    val role: String = "ROLE_USER",
    @field:NotBlank(message = "First name is required")
    val firstname: String,
    val lastname: String,
    @field:Size(min = 12, message = "Password should be at least 6 characters")
    var password: String,
    @field:NotBlank(message = "Password confirmation is required")
    val confirmPassword: String
)