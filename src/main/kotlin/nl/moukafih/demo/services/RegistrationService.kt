package nl.moukafih.demo.services

import nl.moukafih.demo.dtos.RegistrationDTO
import nl.moukafih.demo.entities.User
import nl.moukafih.demo.exceptions.PasswordNotMatchException
import nl.moukafih.demo.repositories.RoleRepository
import nl.moukafih.demo.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Autowired
    lateinit var roleRepository: RoleRepository
    private final val logger: Logger = LoggerFactory.getLogger(RegistrationService::class.java)

    @Transactional
    fun registerUser(registrationDto: RegistrationDTO) {

        if (userRepository.existsByEmail(registrationDto.email)) {
            throw IllegalArgumentException("A user with this email already exists")
        }

        if (registrationDto.password != registrationDto.confirmPassword) {
            throw PasswordNotMatchException("Passwords do not match")
        }

        val role = roleRepository.findByName("ROLE_USER")

        val user = User(
            id = null,
            email = registrationDto.email,
            role = role,
            firstname = registrationDto.firstname,
            lastname = registrationDto.lastname,
            password = passwordEncoder.encode(registrationDto.password),
        )

        userRepository.save(user)

        logger.info("User registered")
    }
}