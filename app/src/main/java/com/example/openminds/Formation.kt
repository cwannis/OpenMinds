package com.example.openminds

data class Formation(
    val id: Int,
    val titre: String,
    val description: String,
    val thematique: String,
    val type: String = "online",
    val imageUrl: String? = "",
    val content: String? = "",
    val videoUrl: String? = "",
    val duration_minutes: Int = 0,
    val created_by: Int? = null,
    val datePubli: String = "",
    val active: Int = 1,
    val sessions_inscrites: Int = 0,
    val quiz_passed: Int = 0,
    val quiz_score: Int = 0,
    val quiz_total: Int = 0
)
