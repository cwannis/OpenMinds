package com.example.openminds.utils

import android.content.Context
import com.example.openminds.formation.Formation

suspend fun getTrendingFormations(context: Context): List<Formation> {
    return dataWebRequete.makeRequest<Formation>("getTrendingFormations.php")
}
