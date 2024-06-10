package nl.moukafih.demo.dtos

import jakarta.validation.constraints.NotBlank
import java.util.*

data class RoleDTO(
    val id: String? = null,
    @field:NotBlank(message = "Name is required")
    val name: String,
    val permissionIds: List<String>,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)