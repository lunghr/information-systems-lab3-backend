package com.lunghr.informationsystemslab1.auth.controllers

import com.lunghr.informationsystemslab1.auth.model.dto.RequestResponseDto
import com.lunghr.informationsystemslab1.auth.model.dto.UserResponseDto
import com.lunghr.informationsystemslab1.auth.services.AdminService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
@Tag(name = "Admin")
class AdminController @Autowired constructor(
    private val adminService: AdminService
) {

    @Operation(summary = "Approve admin request")
    @PostMapping("/approve/{id}")
    fun approveRequest(@PathVariable id: Long) {
        adminService.approveRequest(id)
    }

    @Operation(summary = "Reject admin request")
    @PostMapping("/reject/{id}")
    fun rejectRequest(@PathVariable id: Long) {
        adminService.rejectRequest(id)
    }

    @Operation(summary = "Get all requests")
    @GetMapping("/requests")
    fun getRequests(): List<RequestResponseDto> {
        return adminService.getRequests()
    }

    @Operation(summary = "Get all admins")
    @GetMapping("/admins-list")
    fun getAdmins(): List<UserResponseDto> {
        return adminService.getAdmins()
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users-list")
    fun getUsers(): List<UserResponseDto> {
        return adminService.getUsers()
    }
}
