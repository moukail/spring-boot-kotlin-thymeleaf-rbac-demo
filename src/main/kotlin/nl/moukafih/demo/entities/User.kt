package nl.moukafih.demo.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SourceType
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val role: String,
    @field:NotBlank(message = "First name is required")
    @Column(nullable = false)
    val firstname: String,
    val lastname: String,
){
    @CreationTimestamp(source = SourceType.DB)
    val createdAt: Date = Date()
    @UpdateTimestamp(source = SourceType.DB)
    val updatedAt: Date = Date()
}