package nl.moukafih.demo.controllers

import nl.moukafih.demo.entities.User
import nl.moukafih.demo.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@Controller
class UserController (private val userRepository: UserRepository) {

    @GetMapping("/users")
    fun userIndex(model: Model): String {
        model["users"] = userRepository.findAll()
        return "user/index"
    }

    @GetMapping("/users/add")
    fun showAdd(model: Model) : String {
        model["user"] = User(id = "", email = "", firstname = "", lastname = "", role = "")
        model["message"] = "Add User"
        model["action"] = "/users/add"
        return "user/form"
    }

    @PostMapping("/users/add")
    fun performAdd(@ModelAttribute("user") user: User, model: Model): String {
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
    fun performEdit(@PathVariable id: String, @ModelAttribute user: User, model: Model): String {
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