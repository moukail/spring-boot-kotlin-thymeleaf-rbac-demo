package nl.moukafih.demo.controllers

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import nl.moukafih.demo.dtos.RoleDTO
import nl.moukafih.demo.services.PermissionService
import nl.moukafih.demo.services.RoleService
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
@RequestMapping("/roles")
class RoleController {

    @Autowired
    lateinit var roleService: RoleService
    @Autowired
    lateinit var permissionService: PermissionService

    private final val logger: Logger = LoggerFactory.getLogger(RoleController::class.java)

    @GetMapping
    @PreAuthorize("hasAuthority('role_list')")
    fun roleIndex(model: Model): String {
        model["roles"] = roleService.getAllRoles()
        return "role/index"
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('role_add')")
    fun showAdd(
        @Valid @ModelAttribute("role") roleDTO: RoleDTO?,
        bindingResult: BindingResult,
        model: Model
    ) : String {
        model["form_title"] = "Add Role"
        model["action"] = "/roles/add"
        model["permissions"] = permissionService.getAllPermissions()
        return "role/form"
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('role_add')")
    fun performAdd(
        @Valid @ModelAttribute("role") roleDTO: RoleDTO,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()){
            model["form_title"] = "Add Role"
            model["action"] = "/roles/add"
            model["permissions"] = permissionService.getAllPermissions()
            return "role/form"
        }

        return try {
            roleService.createRole(roleDTO)
            logger.info("Role created successfully: $roleDTO")
            "redirect:/roles"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "error.role", e.message ?: "this role already exists")
            return "role/form"
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('role_edit')")
    fun showEdit(@PathVariable id: String, model: Model) : String {
        try {
            val roleDTO: RoleDTO = roleService.getRoleById(id)
            model["role"] = roleDTO
            model["permissions"] = permissionService.getAllPermissions()
            model["form_title"] = "Edit Role"
            model["action"] = "/roles/$id/edit"
            return "role/form"
        } catch (e : EntityNotFoundException) {
            return "redirect:/roles"
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('role_edit')")
    fun performEdit(
        @PathVariable id: String,
        @Valid @ModelAttribute("role") roleDTO: RoleDTO,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()){
            model["form_title"] = "Edit Role"
            model["action"] = "/roles/$id/edit"
            return "role/form"
        }

        return try {
            roleService.updateRole(id = id, roleDTO = roleDTO)
            logger.info("Updated role: {}", roleDTO)
            "redirect:/roles"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "error.role", e.message ?: "Duplicate role")
            return "role/form"
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('role_delete')")
    fun delete(@PathVariable id: String) : String {
        roleService.deleteRole(id)
        logger.info("Deleted role with id: {}", id)
        return "redirect:/roles"
    }
}