package com.lunghr.informationsystemslab1.auth.services

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.model.ent.User
import com.lunghr.informationsystemslab1.auth.model.repos.UserRepository
import com.lunghr.informationsystemslab1.model.exceptions.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun createUser(user: User): User =
        userRepository.findUserByUsername(user.username)?.let { throw Exception("User already exists") }
            ?: userRepository.save(user)

    fun getUserByUsername(username: String): User =
        userRepository.findUserByUsername(username) ?: throw UserNotFoundException("User not found")

    fun userDetailsService(): UserDetailsService = UserDetailsService { getUserByUsername(it) }

    fun findAdmins(): List<User> = userRepository.findUserByRole(Role.ROLE_ADMIN)

    fun getUserById(id: Long): User = userRepository.findFirstById(id) ?: throw UserNotFoundException("User not found")

    fun makeAdmin(username: String) {
        userRepository.findUserByUsername(username)?.let { user ->
            user.role = Role.ROLE_ADMIN
            userRepository.save(user)
        }
            ?: throw UserNotFoundException("User not found")
    }

    fun findAll(): List<User> = userRepository.findAll()

    fun getIdByUsername(username: String): Long = userRepository.findUserByUsername(username)?.getId()
        ?: throw UserNotFoundException("User not found")
}
