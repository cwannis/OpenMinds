package com.example.openminds

data class Badge(
    val id: Int,
    val titre: String,
    val description: String,
    val datePubli: Long = 0,
    val imageUrl: String = ""
)
