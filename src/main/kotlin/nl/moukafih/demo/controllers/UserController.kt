package nl.moukafih.demo.controllers

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import nl.moukafih.demo.dtos.UserDTO
import nl.moukafih.demo.services.RoleService
import nl.moukafih.demo.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/users")
class UserController (private val userService: UserService) {

    @Autowired
    lateinit var roleService: RoleService

    private final val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping
    @PreAuthorize("hasAuthority('user_list')")
    fun userIndex(model: Model): String {
        model["users"] = userService.getAllUsers()
        return "user/index"
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('user_add')")
    fun showAdd(
        @Valid @ModelAttribute("user") userDto: UserDTO?,
        bindingResult: BindingResult,
        model: Model
    ) : String {
        model["form_title"] = "Add User"
        model["action"] = "/users/add"
        model["roles"] = roleService.getAllRoles()
        return "user/form"
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user_add')")
    fun performAdd(
        @Valid @ModelAttribute("user") userDto: UserDTO,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()){
            model["form_title"] = "Add User"
            model["action"] = "/users/add"
            model["roles"] = roleService.getAllRoles()
            return "user/form"
        }

        return try {
            userService.createUser(userDto)
            logger.info("User created successfully: $userDto")
            "redirect:/users"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("email", "error.user", e.message ?: "this email already exists")
            return "user/form"
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('user_edit')")
    fun showEdit(@PathVariable id: String, model: Model) : String {
        try {
            val userDto: UserDTO = userService.getUserById(id)
            model["user"] = userDto
            model["form_title"] = "Edit User"
            model["action"] = "/users/$id/edit"
            model["roles"] = roleService.getAllRoles()
            return "user/form"
        } catch (e : EntityNotFoundException) {
            return "redirect:/users"
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('user_edit')")
    fun performEdit(
        @PathVariable id: String,
        @Valid @ModelAttribute("user") userDto: UserDTO,
        bindingResult: BindingResult,
        model: Model
    ): String {

        if (bindingResult.hasErrors()){
            model["form_title"] = "Edit User"
            model["action"] = "/users/$id/edit"
            model["roles"] = roleService.getAllRoles()
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
    @PreAuthorize("hasAuthority('user_details')")
    fun details(@PathVariable id: String, model: Model) : String {
        try {
            val user: UserDTO = userService.getUserById(id)
            model["user"] = user
            return "user/details"
        } catch (e : EntityNotFoundException) {
            return "redirect:/users"
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('user_delete')")
    fun deleteUser(@PathVariable id: String) : String {
        userService.deleteUser(id)
        logger.info("Deleted user with id: {}", id)
        return "redirect:/users"
    }
}