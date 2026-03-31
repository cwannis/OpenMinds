package com.example.openminds

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson

object dataWebRequete {

    suspend inline fun <reified T> makeRequest(
        endUrl: String,
        parameterss: Map<String, String> = emptyMap()
    ): List<T> {
        val response: HttpResponse = ApiClient.httpClient.post(baseUrl + endUrl) {
            url {
                for (parameter in parameterss) {
                    parameters.append(parameter.key, parameter.value)
                }
                header("X-Api-Key", BuildConfig.API_KEY)
            }
        }
        val contenu = response.bodyAsText()
        Log.d("API_RES", "Réponse du serveur : $contenu")
        return response.body<List<T>>()
    }

    suspend fun makeRequestWithoutReturn(
        endUrl: String,
        parameterss: Map<String, String> = emptyMap()
    ): Int {
        val response: HttpResponse = ApiClient.httpClient.post(baseUrl + endUrl) {
            url {
                for (parameter in parameterss) {
                    parameters.append(parameter.key, parameter.value)
                }
                header("X-Api-Key", BuildConfig.API_KEY)
            }
        }
        Log.d("API_RES", "Status: ${response.status.value} - ${response.bodyAsText()}")
        return response.status.value
    }
}

object ApiClient {
    val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }
    }
}
