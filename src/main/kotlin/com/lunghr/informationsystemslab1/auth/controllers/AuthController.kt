package com.lunghr.informationsystemslab1.auth.controllers

import com.lunghr.informationsystemslab1.auth.model.ent.AuthRequest
import com.lunghr.informationsystemslab1.auth.model.ent.RegisterRequest
import com.lunghr.informationsystemslab1.auth.model.ent.TokenResponse
import com.lunghr.informationsystemslab1.auth.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
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
    fun login(@RequestBody @Valid authRequest: AuthRequest): TokenResponse {
        return authService.login(authRequest)
    }

    @Operation(summary = "Sign up")
    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): TokenResponse {
        return authService.register(registerRequest)
    }

    @Operation(summary = "Get username from token")
    @GetMapping("/username")
    fun getUsername(@RequestHeader("Authorization") token: String): String {
        return authService.getUsernameFromToken(token)
    }
}
