package nl.moukafih.demo.services

import jakarta.validation.Valid
import nl.moukafih.demo.dtos.UserDto
import nl.moukafih.demo.entities.User
import nl.moukafih.demo.exceptions.UserNotFoundException
import nl.moukafih.demo.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll().map { convertToDto(it) }
    }

    fun getUserById(id: String): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }
        return convertToDto(user)
    }

    @Transactional
    fun createUser(@Valid userDto: UserDto): UserDto {

        if (userRepository.existsByEmail(userDto.email)) {
            throw IllegalArgumentException("A user with this email already exists")
        }

        val user = convertToEntity(userDto)
        userRepository.save(user)
        return convertToDto(user)
    }

    @Transactional
    fun updateUser(id: String, @Valid userDto: UserDto): UserDto {
        val currentUser : User = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }

        if (currentUser.email != userDto.email && userRepository.existsByEmail(userDto.email)) {
            throw IllegalArgumentException("A user with this email already exists")
        }

        val user = convertToEntity(userDto)
        userRepository.save(user)
        return convertToDto(user)
    }

    @Transactional
    fun deleteUser(id: String) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }
        userRepository.delete(user)
    }

    private fun convertToDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            email = user.email,
            role = user.role,
            firstname = user.firstname,
            lastname = user.lastname,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }

    private fun convertToEntity(userDto: UserDto): User {
        return User(
            id = userDto.id,
            email = userDto.email,
            role = userDto.role,
            firstname = userDto.firstname,
            lastname = userDto.lastname,
        )
    }
}