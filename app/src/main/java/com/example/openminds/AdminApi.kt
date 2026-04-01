package com.example.openminds

import android.content.Context
import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText

suspend fun getAdminDashboard(context: Context, userId: Int): Map<String, Any> {
    val response = ApiClient.httpClient.post(baseUrl + "getAdminDashboard.php") {
        url {
            parameters.append("user_id", userId.toString())
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    }
    val text = response.bodyAsText()
    Log.d("API_RES", "Admin dashboard: $text")
    return response.body<Map<String, Any>>()
}
