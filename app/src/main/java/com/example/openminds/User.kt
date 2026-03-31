package com.example.openminds

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val organization: String = "",
    val ppLink: String? = "",
    val role: String = "user",
    val association_id: Int? = null
)
