package com.example.springmvc.entity

import javax.persistence.*


@Entity
@Table(name = "roles")
class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
    private val name: String? = null

    fun getName(): String? {
        return name
    }
}