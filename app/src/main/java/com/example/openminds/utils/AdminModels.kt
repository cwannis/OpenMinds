package com.example.openminds.utils

data class AdminStats(
    val nb_benevoles: Int = 0,
    val nb_formateurs: Int = 0,
    val nb_formations: Int = 0,
    val nb_sessions: Int = 0,
    val nb_inscriptions: Int = 0,
    val nb_quiz_passes: Int = 0,
    val nb_quiz_reussis: Int = 0,
    val taux_reussite: Int = 0
)

data class AdminInscription(
    val name: String = "",
    val formation_titre: String = "",
    val status: String = "",
    val inscrit_le: String = ""
)
