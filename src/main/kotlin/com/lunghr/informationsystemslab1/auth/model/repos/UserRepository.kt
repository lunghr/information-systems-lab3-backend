package com.lunghr.informationsystemslab1.auth.model.repos

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.model.ent.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByUsername(username: String): User?
    fun findUserByRole(role: Role): List<User>
    fun findFirstById(id: Long): User?
}
