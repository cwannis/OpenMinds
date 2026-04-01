package com.example.openminds.session

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String = "benevole",
    val ppLink: String? = ""
)
