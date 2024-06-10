package nl.moukafih.demo.services

import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import nl.moukafih.demo.dtos.RoleDTO
import nl.moukafih.demo.entities.Role
import nl.moukafih.demo.repositories.PermissionRepository
import nl.moukafih.demo.repositories.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoleService {

    @Autowired
    lateinit var roleRepository: RoleRepository
    @Autowired
    lateinit var permissionRepository: PermissionRepository

    fun getAllRoles(): List<RoleDTO> {
        return roleRepository.findAll().map { convertToDto(it) }
    }

    fun getRoleById(id: String): RoleDTO {
        val role = roleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Role not found with id: $id") }
        return convertToDto(role)
    }

    @Transactional
    fun createRole(@Valid roleDTO: RoleDTO) {
        if (roleRepository.existsByName(roleDTO.name)) {
            throw IllegalArgumentException("A role with this name already exists")
        }

        val role = convertToEntity(roleDTO)
        roleRepository.save(role)
    }

    @Transactional
    fun updateRole(id: String, @Valid roleDTO: RoleDTO) {
        val currentRole : Role = roleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Role not found with id: $id") }

        if (currentRole.name != roleDTO.name && roleRepository.existsByName(roleDTO.name)) {
            throw IllegalArgumentException("A role with this name already exists")
        }

        val role = convertToEntity(roleDTO)
        roleRepository.save(role)
    }

    @Transactional
    fun deleteRole(id: String) {
        val role = roleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Role not found with id: $id") }
        roleRepository.delete(role)
    }

    private fun convertToDto(role: Role): RoleDTO {
        return RoleDTO(
            id = role.id,
            name = role.name,
            createdAt = role.createdAt,
            updatedAt = role.updatedAt,
            permissionIds = role.permissions.mapNotNull { it.id }
        )
    }

    private fun convertToEntity(roleDTO: RoleDTO): Role {
        val permissions = permissionRepository.findAllById(roleDTO.permissionIds).toSet()
        return Role(id = roleDTO.id, name = roleDTO.name, permissions = permissions.toMutableList())
    }
}