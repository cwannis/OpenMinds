package com.example.openminds

data class Inscription(
    val id: Int,
    val session_id: Int,
    val status: String = "inscrit",
    val inscrit_le: String = "",
    val formation_id: Int = 0,
    val formation_titre: String = "",
    val thematique: String = "",
    val date_debut: String = "",
    val date_fin: String = "",
    val location: String? = "",
    val is_online: Int = 0,
    val meeting_link: String? = "",
    val formateur_name: String = ""
)
