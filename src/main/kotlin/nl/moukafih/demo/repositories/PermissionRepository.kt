package nl.moukafih.demo.repositories

import nl.moukafih.demo.entities.Permission
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, String> {}