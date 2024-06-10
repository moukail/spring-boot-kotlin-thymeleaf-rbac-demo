package nl.moukafih.demo.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SourceType
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
data class Role(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @field:NotBlank(message = "Name is required")
    @Column(nullable = false, unique = true)
    val name: String,
    @ManyToMany(fetch = FetchType.EAGER)
    val permissions: MutableList<Permission> = mutableListOf()
){
    @CreationTimestamp(source = SourceType.DB)
    val createdAt: Date = Date()
    @UpdateTimestamp(source = SourceType.DB)
    val updatedAt: Date = Date()
}