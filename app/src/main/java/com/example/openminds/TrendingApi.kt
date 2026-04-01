package com.example.openminds

import android.content.Context

suspend fun getTrendingFormations(context: Context): List<Formation> {
    return dataWebRequete.makeRequest<Formation>("getTrendingFormations.php")
}
