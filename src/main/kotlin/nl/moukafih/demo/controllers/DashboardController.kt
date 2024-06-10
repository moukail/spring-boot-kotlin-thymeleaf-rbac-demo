package nl.moukafih.demo.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@PreAuthorize("hasRole('USER')")
class DashboardController {
    @GetMapping("/dashboard", name = "dashboard")
    fun index(): String {
        return "dashboard/index"
    }
}
