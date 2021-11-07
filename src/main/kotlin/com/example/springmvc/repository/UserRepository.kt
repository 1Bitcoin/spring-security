package com.example.springmvc.repository

import com.example.springmvc.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface UserRepository : CrudRepository<User?, Long?> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    fun getUserByUsername(@Param("username") username: String?): User?
}