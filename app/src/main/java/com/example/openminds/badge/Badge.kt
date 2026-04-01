package com.example.openminds.badge

data class Badge(
    val id: Int,
    val titre: String,
    val description: String,
    val imageUrl: String = "",
    val thematique: String = "",
    val dateObtention: String = ""
)
