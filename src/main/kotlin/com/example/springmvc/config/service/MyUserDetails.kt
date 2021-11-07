package com.example.springmvc.config.service

import com.example.springmvc.entity.Role
import com.example.springmvc.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

import org.springframework.security.core.userdetails.UserDetails


class MyUserDetails(user: User) : UserDetails {
    private val user: User
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        val roles: Set<Role> = user.getRoles()
        val authorities: MutableList<SimpleGrantedAuthority?> = ArrayList()
        for (role in roles) {
            authorities.add(SimpleGrantedAuthority(role.getName()))
        }
        return authorities
    }

    override fun getPassword(): String? {
        return user.getPassword()
    }

    override fun getUsername(): String? {
        return user.getUsername()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return user.isEnabled()
    }

    init {
        this.user = user
    }
}