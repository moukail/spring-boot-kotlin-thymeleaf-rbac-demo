package nl.moukafih.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            //csrf { disable() }
            authorizeHttpRequests {
                authorize("/webjars/**", permitAll)
                authorize("/js/**", permitAll)
                authorize("/css/**", permitAll)
                authorize("/images/**", permitAll)
                authorize("/register", permitAll)
                authorize("/login", permitAll)
                authorize("/", permitAll)
                //authorize("/dashboard", hasRole("USER"))
                //authorize("/users", hasAuthority("user_list"))
                authorize(anyRequest, authenticated)
            }
            formLogin {
                loginPage = "/login"
                failureUrl = "/login?error=true"
                defaultSuccessUrl("/dashboard", true)
                permitAll()
            }
            logout {
                logoutUrl = "/logout"
                logoutSuccessUrl = "/login?logout=true"
                permitAll()
            }
        }
        return http.build()
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        //roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER > ROLE_ANONYMOUS")
        roleHierarchy.setHierarchy(
            """
            ROLE_ADMIN > ROLE_MANAGER
            ROLE_MANAGER > ROLE_USER
            ROLE_USER > ROLE_ANONYMOUS
        """.trimIndent()
        )
        return roleHierarchy
    }

    @Bean
    fun methodSecurityExpressionHandler(roleHierarchy: RoleHierarchy) : MethodSecurityExpressionHandler {
        val handler = DefaultMethodSecurityExpressionHandler()
        handler.setRoleHierarchy(roleHierarchy)
        return handler
    }
}