package nl.moukafih.demo.services

import nl.moukafih.demo.dtos.PermissionDTO
import nl.moukafih.demo.repositories.PermissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService {

    @Autowired
    lateinit var permissionRepository: PermissionRepository

    fun getAllPermissions(): List<PermissionDTO> {
        return permissionRepository.findAll().map { PermissionDTO(id = it.id, name = it.name) }
    }
}