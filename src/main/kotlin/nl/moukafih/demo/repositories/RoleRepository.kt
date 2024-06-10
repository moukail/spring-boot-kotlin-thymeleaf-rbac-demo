package nl.moukafih.demo.repositories

import nl.moukafih.demo.entities.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role
    fun existsByName(name: String): Boolean
}