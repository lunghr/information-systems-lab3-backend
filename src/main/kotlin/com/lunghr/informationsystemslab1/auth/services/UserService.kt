package com.lunghr.informationsystemslab1.auth.services

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.model.ent.User
import com.lunghr.informationsystemslab1.auth.model.repos.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun createUser(user: User): User =
        userRepository.findUserByUsername(user.username)?.let { throw Exception("User already exists") }
            ?: userRepository.save(user)

    fun getUserByUsername(username: String): User =
        userRepository.findUserByUsername(username) ?: throw Exception("User not found")

    fun userDetailsService(): UserDetailsService = UserDetailsService { getUserByUsername(it) }


    @Deprecated("This is for testing purpose only")
    fun getAdmin() {
        val user = getUserByUsername(SecurityContextHolder.getContext().authentication.name)
        user.setRole(Role.ROLE_ADMIN)
        userRepository.save(user)
    }

}