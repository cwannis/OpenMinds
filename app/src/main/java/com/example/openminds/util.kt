package com.example.openminds

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.edit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson

public val baseUrl: String = "http://10.0.2.2:8080/openMinds/phpFile/"
private val client = HttpClient(Android) {
    install(ContentNegotiation) { gson() }
}

fun isLogged(context: Context): Boolean {
    val sp = context.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    return sp.getBoolean("IS_LOGGED", false)
}

fun getUserRole(context: Context): String {
    val sp = context.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    return sp.getString("USER_ROLE", "benevole") ?: "benevole"
}

fun getUserId(context: Context): Int {
    val sp = context.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    return sp.getInt("USER_ID", 0)
}

fun logout(where: Context) {
    val sp = where.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    sp.edit { clear() }
    val intent = Intent(where, LogInActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    where.startActivity(intent)
    if (where is Activity) where.finish()
}

fun login(context: Context, user: User) {
    Log.i("API_RES", "user ${user.name} role=${user.role}")
    val sp = context.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    sp.edit {
        putInt("USER_ID", user.id)
        putString("USER_NAME", user.name)
        putString("USER_EMAIL", user.email)
        putString("USER_ROLE", user.role)
        putBoolean("IS_LOGGED", true)
        if (!user.ppLink.isNullOrEmpty()) putString("USER_PP", user.ppLink)
    }
    val intent = when (user.role) {
        "formateur" -> Intent(context, FormateurDashboardActivity::class.java)
        "admin" -> Intent(context, AdminDashboardActivity::class.java)
        else -> Intent(context, MainActivity::class.java)
    }
    context.startActivity(intent)
}

fun isEmailValid(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

suspend fun login(context: Context, mail: String, psw: String) {
    val response = client.post(baseUrl + "getUserData.php") {
        url {
            parameters.append("email", mail)
            parameters.append("password", psw)
        }
        header("X-Api-Key", BuildConfig.API_KEY)
    }
    val text = response.bodyAsText()
    Log.d("API_RES", "Login: $text")
    if (response.status.value == 200) {
        val users = response.body<List<User>>()
        if (users.isNotEmpty()) login(context, users[0])
    } else {
        Toast.makeText(context, "Mail ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
    }
}

suspend fun getUserData(context: Context, id: Int): User {
    val res = dataWebRequete.makeRequest<User>("getUserData.php", mapOf("id" to id.toString()))
    return if (res.isNotEmpty()) res[0] else User(-1, "", "", "benevole")
}

suspend fun signUp(context: Context, name: String, email: String, psw: String) {
    val status = dataWebRequete.makeRequestWithoutReturn("mailExist.php", mapOf("email" to email))
    if (status == 409) {
        Toast.makeText(context, "Cet email est deja utilise", Toast.LENGTH_SHORT).show()
        return
    }
    val status2 = dataWebRequete.makeRequestWithoutReturn("createUser.php", mapOf(
        "name" to name, "mail" to email, "password" to psw
    ))
    if (status2 != 401) login(context, email, psw)
}

suspend fun sendCodeMail(code: String, mail: String) {
    dataWebRequete.makeRequestWithoutReturn("mailRecup.php", mapOf("code" to code, "mailTo" to mail))
}

suspend fun changePassword(context: Context, mail: String, psw: String) {
    dataWebRequete.makeRequestWithoutReturn("changePassword.php", mapOf("email" to mail, "newpsw" to psw))
    context.startActivity(Intent(context, LogInActivity::class.java))
}

suspend fun getAllFormations(context: Context, thematique: String = "", search: String = ""): List<Formation> {
    val params = mutableMapOf<String, String>()
    if (thematique.isNotEmpty()) params["thematique"] = thematique
    if (search.isNotEmpty()) params["search"] = search
    return dataWebRequete.makeRequest<Formation>("getAllFormations.php", params)
}

suspend fun getFormation(context: Context, formationId: Int): Formation {
    val response = ApiClient.httpClient.post(baseUrl + "getFormation.php") {
        url {
            parameters.append("formation_id", formationId.toString())
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    }
    return response.body<Formation>()
}

suspend fun getThematiques(context: Context): List<String> {
    val raw = dataWebRequete.makeRequest<Map<String, String>>("getThematiques.php")
    return raw.map { it["thematique"] ?: "" }.filter { it.isNotEmpty() }
}

suspend fun getSessions(context: Context, formationId: Int): List<Session> {
    return dataWebRequete.makeRequest<Session>("getSessions.php", mapOf("formation_id" to formationId.toString()))
}

suspend fun getMyInscriptions(context: Context, userId: Int): List<Inscription> {
    return dataWebRequete.makeRequest<Inscription>("getMyInscriptions.php", mapOf("user_id" to userId.toString()))
}

suspend fun inscrireSession(context: Context, userId: Int, sessionId: Int): Int {
    return dataWebRequete.makeRequestWithoutReturn("inscrireSession.php", mapOf(
        "user_id" to userId.toString(), "session_id" to sessionId.toString()
    ))
}

suspend fun desinscrireSession(context: Context, userId: Int, sessionId: Int): Int {
    return dataWebRequete.makeRequestWithoutReturn("desinscrireSession.php", mapOf(
        "user_id" to userId.toString(), "session_id" to sessionId.toString()
    ))
}

suspend fun getQuiz(context: Context, formationId: Int): List<Quiz> {
    val response = ApiClient.httpClient.post(baseUrl + "getQuiz.php") {
        url {
            parameters.append("formation_id", formationId.toString())
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    }
    val text = response.bodyAsText()
    Log.d("API_RES", "Quiz: $text")
    return response.body<List<Quiz>>()
}

data class QuizSubmitResult(val score: Int, val total_questions: Int, val passed: Int, val percentage: Int)

suspend fun submitQuiz(context: Context, userId: Int, quizId: Int, answers: String): QuizSubmitResult {
    val response = ApiClient.httpClient.post(baseUrl + "submitQuiz.php") {
        url {
            parameters.append("user_id", userId.toString())
            parameters.append("quiz_id", quizId.toString())
            parameters.append("answers", answers)
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    }
    return response.body<QuizSubmitResult>()
}

suspend fun getMyProgression(context: Context, userId: Int): List<Formation> {
    return dataWebRequete.makeRequest<Formation>("getMyProgression.php", mapOf("user_id" to userId.toString()))
}

suspend fun getMyBadges(context: Context, userId: Int): List<Badge> {
    return dataWebRequete.makeRequest<Badge>("getMyBadges.php", mapOf("user_id" to userId.toString()))
}
