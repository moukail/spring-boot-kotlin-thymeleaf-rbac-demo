package nl.moukafih.demo.repositories

import nl.moukafih.demo.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String>{
    fun findByEmail(email : String) : User?
}