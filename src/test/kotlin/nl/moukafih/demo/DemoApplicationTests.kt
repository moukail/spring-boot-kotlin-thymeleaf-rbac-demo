package nl.moukafih.demo

import nl.moukafih.demo.controllers.HomeController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RbacDemoApplicationTests @Autowired constructor(
	val homeController: HomeController
) {

	@Test
	@DisplayName("First test")
	fun contextLoads() {
		assertThat(homeController).isNotNull
	}

}