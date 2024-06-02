package nl.moukafih.demo.repositories

import nl.moukafih.demo.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email : String) : User?
    fun existsByEmail(email: String): Boolean
}