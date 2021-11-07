package com.example.springmvc.config.service

import com.example.springmvc.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException


class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository!!.getUserByUsername(username)
            ?: throw UsernameNotFoundException("Could not find user with name = $username")
        return MyUserDetails(user)
    }
}