package nl.moukafih.demo.services

import nl.moukafih.demo.dtos.RegistrationDto
import nl.moukafih.demo.entities.User
import nl.moukafih.demo.exceptions.EmailNotUniqueException
import nl.moukafih.demo.exceptions.PasswordNotMatchException
import nl.moukafih.demo.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SecurityService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private final val logger: Logger = LoggerFactory.getLogger(SecurityService::class.java)

    @Transactional
    fun registerUser(registrationDto: RegistrationDto) {

        if (userRepository.existsByEmail(registrationDto.email)) {
            throw EmailNotUniqueException("A user with this email already exists")
        }

        if (registrationDto.password != registrationDto.confirmPassword) {
            throw PasswordNotMatchException("Passwords do not match")
        }

        val user = User(
            id = null,
            email = registrationDto.email,
            role = registrationDto.role,
            firstname = registrationDto.firstname,
            lastname = registrationDto.lastname,
            password = passwordEncoder.encode(registrationDto.password),
        )

        userRepository.save(user)

        logger.info("User registered")
    }
}