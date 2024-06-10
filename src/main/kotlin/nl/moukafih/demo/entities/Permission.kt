package nl.moukafih.demo.entities

import jakarta.persistence.*

@Entity
data class Permission(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(nullable = false, unique = true)
    val name: String
)