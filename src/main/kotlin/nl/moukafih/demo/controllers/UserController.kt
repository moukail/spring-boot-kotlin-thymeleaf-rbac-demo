package nl.moukafih.demo.controllers

import jakarta.validation.Valid
import nl.moukafih.demo.entities.User
import nl.moukafih.demo.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
class UserController (private val userRepository: UserRepository) {

    private final val log: Logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/users")
    fun userIndex(model: Model): String {
        log.info("--------------userIndex-----------------")
        model["message"] = "Users"
        model["users"] = userRepository.findAll()
        return "user/index"
    }

    @GetMapping("/users/add")
    fun showAdd(@ModelAttribute("user") user: User?, bindingResult: BindingResult, model: Model) : String {
        model["message"] = "Add User"
        model["action"] = "/users/add"
        return "user/form"
    }

    @PostMapping("/users/add")
    fun performAdd(@Valid @ModelAttribute("user") user: User, bindingResult: BindingResult, model: Model): String {

        if (userRepository.existsByEmail(user.email)) {
            bindingResult.rejectValue("email", "error.user", "Duplicate email")
        }

        if (bindingResult.hasErrors()){
            model["message"] = "Add User"
            model["user"] = user
            model["action"] = "/users/add"
            return "user/form"
        }

        userRepository.save(user)
        return "redirect:/users"
    }

    @GetMapping("/users/{id}/edit")
    fun showEdit(@PathVariable id: String, model: Model) : String {

        val user: User = userRepository.findByIdOrNull(id) ?: return "redirect:/users"

        model["message"] = "Edit User"
        model["user"] = user
        model["action"] = "/users/$id/edit"

        return "user/form"
    }

    @PostMapping("/users/{id}/edit")
    fun performEdit(@PathVariable id: String, @Valid @ModelAttribute user: User, bindingResult: BindingResult, model: Model): String {

        val currentUser : User = userRepository.findById(id).get()

        if (currentUser.email != user.email && userRepository.existsByEmail(user.email)) {
            bindingResult.rejectValue("email", "error.user", "Duplicate email")
        }

        if (bindingResult.hasErrors()){
            model["message"] = "Edit User"
            model["user"] = user
            model["action"] = "/users/$id/edit"
            return "user/form"
        }

        userRepository.save(user)
        return "redirect:/users"
    }

    @GetMapping("/users/{id}")
    fun details(@PathVariable id: String, model: Model) : String {
        val user: User = userRepository.findByIdOrNull(id) ?: return "redirect:/users"
        model["user"] = user
        return "user/details"
    }

    @PostMapping("/users/{id}/delete")
    fun performDelete(@PathVariable id: String, model: Model) : String {
        val user: User = userRepository.findByIdOrNull(id) ?: return "redirect:/users"
        userRepository.delete(user)
        return "redirect:/users"
    }
}