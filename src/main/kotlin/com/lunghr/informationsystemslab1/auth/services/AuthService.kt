package com.lunghr.informationsystemslab1.auth.services

import com.lunghr.informationsystemslab1.auth.model.ent.AuthRequest
import com.lunghr.informationsystemslab1.auth.model.ent.RegisterRequest
import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.model.ent.TokenResponse
import com.lunghr.informationsystemslab1.auth.model.ent.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var jwtService: JwtService
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    fun register(request: RegisterRequest): TokenResponse {
        val user = User(
            id = 0L, // This is a plug value pupupupu
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = Role.ROLE_USER
        )

        userService.createUser(user)
        println("User created")
        return TokenResponse(jwtService.generateToken(user))
    }

    fun login(request: AuthRequest): TokenResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val user = userService.userDetailsService().loadUserByUsername(request.username) as User
        return TokenResponse(jwtService.generateToken(user))
    }
}
