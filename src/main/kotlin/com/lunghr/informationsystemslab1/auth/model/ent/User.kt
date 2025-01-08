package com.lunghr.informationsystemslab1.auth.model.ent

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long,

    @Column(name = "username", unique = true, nullable = false)
    private var username: String,

    @Column(name = "password", nullable = false)
    private var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private var role: Role = Role.ROLE_USER
) : UserDetails {
    // Return list of roles
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role.name))
    }

    // Return password
    override fun getPassword(): String {
        return password
    }

    // Return username
    override fun getUsername(): String {
        return username
    }
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true


    fun getId() : Long{
        return id
    }

    fun getRole() : Role{
        return role
    }

    fun setRole(newRole: Role) {
        role = newRole
    }

}