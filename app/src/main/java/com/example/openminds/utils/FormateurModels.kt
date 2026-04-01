package com.example.openminds.utils

data class FormateurSession(
    val id: Int,
    val formation_id: Int,
    val formation_titre: String = "",
    val thematique: String = "",
    val date_debut: String = "",
    val date_fin: String = "",
    val location: String? = "",
    val is_online: Int = 0,
    val meeting_link: String? = "",
    val nb_inscrits: Int = 0
)

data class Participant(
    val inscription_id: Int,
    val user_id: Int,
    val name: String = "",
    val email: String = "",
    val status: String = "inscrit"
)

data class FormateurStat(
    val id: Int,
    val titre: String = "",
    val thematique: String = "",
    val nb_sessions: Int = 0,
    val nb_inscrits: Int = 0,
    val nb_quiz_passes: Int = 0,
    val nb_quiz_reussis: Int = 0
)
