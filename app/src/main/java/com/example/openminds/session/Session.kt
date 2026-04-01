package com.example.openminds.session

data class Session(
    val id: Int,
    val formation_id: Int,
    val formateur_id: Int? = null,
    val formateur_name: String? = "",
    val date_debut: String = "",
    val date_fin: String = "",
    val location: String? = "",
    val max_participants: Int = 0,
    val is_online: Int = 0,
    val meeting_link: String? = "",
    val active: Int = 1
)
