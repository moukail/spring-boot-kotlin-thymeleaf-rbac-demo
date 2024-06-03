package nl.moukafih.demo.controllers

import jakarta.validation.Valid
import nl.moukafih.demo.dtos.UserDto
import nl.moukafih.demo.exceptions.EmailNotUniqueException
import nl.moukafih.demo.exceptions.UserNotFoundException
import nl.moukafih.demo.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/users")
class UserController (private val userService: UserService) {

    private final val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping
    fun userIndex(model: Model): String {
        model["users"] = userService.getAllUsers()
        return "user/index"
    }

    @GetMapping("/add")
    fun showAdd(
        @Valid @ModelAttribute("user") userDto: UserDto?,
        bindingResult: BindingResult,
        model: Model
    ) : String {
        model["form_title"] = "Add User"
        model["action"] = "/users/add"
        return "user/form"
    }

    @PostMapping("/add")
    fun performAdd(
        @Valid @ModelAttribute("user") userDto: UserDto,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (bindingResult.hasErrors()){
            model["form_title"] = "Add User"
            model["action"] = "/users/add"
            return "user/form"
        }

        return try {
            userService.createUser(userDto)
            logger.info("User created successfully: $userDto")
            "redirect:/users"
        } catch (e: EmailNotUniqueException) {
            bindingResult.rejectValue("email", "error.user", e.message ?: "this email already exists")
            return "user/form"
        }
    }

    @GetMapping("/{id}/edit")
    fun showEdit(@PathVariable id: String, model: Model) : String {
        try {
            val userDto: UserDto = userService.getUserById(id)
            model["user"] = userDto
            model["form_title"] = "Edit User"
            model["action"] = "/users/$id/edit"
            return "user/form"
        } catch (e : UserNotFoundException) {
            return "redirect:/users"
        }
    }

    @PostMapping("/{id}/edit")
    fun performEdit(
        @PathVariable id: String,
        @Valid @ModelAttribute("user") userDto: UserDto,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (bindingResult.hasErrors()){
            model["form_title"] = "Edit User"
            model["action"] = "/users/$id/edit"
            return "user/form"
        }

        return try {
            userService.updateUser(id, userDto)
            logger.info("Updated user: {}", userDto)
            "redirect:/users"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("email", "error.user", e.message ?: "Duplicate email")
            return "user/form"
        }
    }

    @GetMapping("/{id}")
    fun details(@PathVariable id: String, model: Model) : String {
        try {
            val user: UserDto = userService.getUserById(id)
            model["user"] = user
            return "user/details"
        } catch (e : UserNotFoundException) {
            return "redirect:/users"
        }
    }

    @PostMapping("/{id}/delete")
    fun deleteUser(@PathVariable id: String) : String {
        userService.deleteUser(id)
        logger.info("Deleted user with id: {}", id)
        return "redirect:/users"
    }
}