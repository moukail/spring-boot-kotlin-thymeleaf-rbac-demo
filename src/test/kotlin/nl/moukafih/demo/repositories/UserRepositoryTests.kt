package nl.moukafih.demo.repositories

import nl.moukafih.demo.entities.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class UserRepositoryTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository) {

    @Test
    @DisplayName("User test")
    fun `When findByIdOrNull then return Article`() {
        val user = User(email = "johnDoe", password = "", role = "USER", firstname = "John", lastname = "Doe")
        entityManager.persist(user)
        entityManager.flush()

        val found = userRepository.findByIdOrNull(user.id!!)
        assertThat(found).isEqualTo(user)
    }

    @Test
    fun `When findByUsername then return User`() {
        val user = User(email = "johnDoe", password = "", role = "USER", firstname = "John", lastname = "Doe")
        entityManager.persist(user)
        entityManager.flush()

        val found = userRepository.findByEmail(user.email)
        assertThat(found).isEqualTo(user)
    }

    @Test
    fun deleteUsers() {
        val user = User(email = "johnDoe", password = "", role = "USER", firstname = "John", lastname = "Doe")
        entityManager.persist(user)
        entityManager.flush()

        userRepository.deleteAll()

        assertThat(userRepository.count()).isEqualTo(0)
    }
}