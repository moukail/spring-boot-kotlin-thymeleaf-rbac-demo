package nl.moukafih.demo.services

import jakarta.validation.Valid
import nl.moukafih.demo.dtos.UserDto
import nl.moukafih.demo.entities.User
import nl.moukafih.demo.exceptions.EmailNotUniqueException
import nl.moukafih.demo.exceptions.UserNotFoundException
import nl.moukafih.demo.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private final val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    private val random = SecureRandom()

    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll().map { convertToDto(it) }
    }

    fun getUserById(id: String): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }
        return convertToDto(user)
    }

    @Transactional
    fun createUser(@Valid userDto: UserDto) {

        if (userRepository.existsByEmail(userDto.email)) {
            throw EmailNotUniqueException("A user with this email already exists")
        }

        val user = convertToEntity(userDto)
        val randomPassword = generateRandomPassword()
        logger.info("User password: $randomPassword")
        user.password = passwordEncoder.encode(randomPassword)
        userRepository.save(user)
    }

    @Transactional
    fun updateUser(id: String, @Valid userDto: UserDto) {
        val currentUser : User = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }

        if (currentUser.email != userDto.email && userRepository.existsByEmail(userDto.email)) {
            throw IllegalArgumentException("A user with this email already exists")
        }

        val user = convertToEntity(userDto)
        userRepository.save(user)
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
            password = "",
        )
    }

    private fun generateRandomPassword(): String {
        val password = ByteArray(10)
        random.nextBytes(password)
        return Base64.getUrlEncoder().encodeToString(password)
    }
}