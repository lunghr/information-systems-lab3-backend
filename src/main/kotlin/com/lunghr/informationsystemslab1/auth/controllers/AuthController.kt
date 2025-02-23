package com.lunghr.informationsystemslab1.auth.controllers

import com.lunghr.informationsystemslab1.auth.model.dto.AuthRequest
import com.lunghr.informationsystemslab1.auth.model.dto.AuthResponse
import com.lunghr.informationsystemslab1.auth.model.dto.RegisterRequest
import com.lunghr.informationsystemslab1.auth.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = ["*"])
@Tag(name = "Authentication")
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @Operation(summary = "Sign in")
    @PostMapping("/login")
    fun login(@RequestBody @Valid authRequest: AuthRequest): AuthResponse {
        return authService.login(authRequest)
    }

    @Operation(summary = "Sign up")
    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): AuthResponse {
        return authService.register(registerRequest)
    }
    @Operation(summary = "Get username from token")
    @GetMapping("/username")
    fun getUsername(@RequestHeader("Authorization") token: String): String {
        return authService.getUsernameFromToken(token)
    }

    @Operation(summary = "Get username from id")
    @GetMapping("/username/{id}")
    fun getUsernameById(@PathVariable id: Long): String {
        return authService.getUsernameFromId(id)
    }

    @Operation(summary = "Get user role")
    @GetMapping("/role")
    fun getRole(@RequestHeader("Authorization") token: String): String {
        return authService.getRoleFromToken(token)
    }

    @Operation(summary = "Refresh token")
    @GetMapping("/refresh")
    fun refreshToken(@RequestHeader("Authorization") token: String): String {
        return authService.refreshToken(token)
    }
}
