package nl.moukafih.demo.controllers

import jakarta.validation.Valid
import nl.moukafih.demo.dtos.RegistrationDto
import nl.moukafih.demo.exceptions.EmailNotUniqueException
import nl.moukafih.demo.exceptions.PasswordNotMatchException
import nl.moukafih.demo.services.SecurityService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class AuthController(private val securityService: SecurityService) {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/register")
    fun register(
        @Valid @ModelAttribute("registration") registrationDto: RegistrationDto?,
        bindingResult: BindingResult
    ): String {
        return "register"
    }

    @PostMapping("/register")
    fun performRegistration(
        @Valid @ModelAttribute("registration") registrationDto: RegistrationDto,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (bindingResult.hasErrors()) {
            return "register"
        }

        try {
            securityService.registerUser(registrationDto)
            return "redirect:/register?success"
        } catch (e: PasswordNotMatchException) {
            bindingResult.rejectValue("confirmPassword", "error.user", e.message ?: "Passwords do not match")
        } catch (e: EmailNotUniqueException) {
            bindingResult.rejectValue("email", "error.user", e.message ?: "this email already exists")
        }

        return "register"
    }
}