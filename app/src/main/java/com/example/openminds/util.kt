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

public val baseUrl : String = "http://10.0.2.2:8080/openMinds/phpFile/"
private val client = HttpClient(Android) {
    install(ContentNegotiation) {
        gson()
    }
}
fun isLogged(context: Context) : Boolean{
    val sharedPref = context.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    return sharedPref.getBoolean("IS_LOGGED", false)
}
fun logout(where : Context) {
    val sharedPref = where.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    sharedPref.edit {
        clear()
    }
    val intent = Intent(where, LogInActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    where.startActivity(intent)
    if (where is Activity) {
        where.finish()
    }
}
fun login(context: Context, user: User)
{
    Log.i("API_RES", "utilisateur " + user.name)
    val sharedPref = context.getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
    sharedPref.edit {
        putInt("USER_ID", user.id)
        putString("USER_NAME", user.name)
        putBoolean("IS_LOGGED", true)
    }
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

suspend fun getAllForm(context: Context) : List<Formation>
{
    val response: HttpResponse = client.post(baseUrl + "getAllFormation.php") {
        url {
        }
        header("X-Api-Key", BuildConfig.API_KEY)
    }

    val contenu = response.bodyAsText()
    Log.d("API_RES", "Réponse du serveur : $contenu")

    if(response.status.value == 200)
    {
        val formations = response.body<List<Formation>>();
        return formations
    }
    else
    {
        return ArrayList<Formation>()
    }
}

suspend fun login(context: Context, mail : String, psw : String)
{
    val response: HttpResponse = client.post(baseUrl + "getUserData.php") {
        url {
            parameters.append("email", mail)
            parameters.append("password", psw)
        }
        header("X-Api-Key", BuildConfig.API_KEY)
    }

    val contenu = response.bodyAsText()
    Log.d("API_RES", "Réponse du serveur : $contenu")

    if(response.status.value == 200)
    {
        val users = response.body<List<User>>();
        if(users.isNotEmpty()) {
            val user = users[0]
            login(context, user)
        }
    } else
    {
        Toast.makeText(context, "le mail ou le mot de passe est incorect", Toast.LENGTH_SHORT).show()
    }
}

suspend fun getUserData(context: Context, id : Int) : User{
    val response: HttpResponse = client.post(baseUrl + "getUserData.php") {
        url {
            parameters.append("id", id.toString())
        }
        header("X-Api-Key", BuildConfig.API_KEY)
    }

    val contenu = response.bodyAsText()
    Log.d("API_RES", "Réponse du serveur : $contenu")
    val users = response.body<List<User>>();
    if(users.isNotEmpty()) {
        val user = users[0];
        return user
    }
    return User(-1, "", "", "", "")
}

suspend fun getBadgesOfUser(context: Context, id : Int) : List<Badge> {
    val response: HttpResponse = client.post(baseUrl + "getBadgesOfUser.php") {
        url {
            parameters.append("id", id.toString())
        }
        header("X-Api-Key", BuildConfig.API_KEY)
    }

    val contenu = response.bodyAsText()
    Log.d("API_RES", "Réponse du serveur : $contenu")

    val badges = response.body<List<Badge>>();
    return badges
}

suspend fun signUp(context: Context, nameValue : String, emailValue : String, pswValue : String, orga : String)
{
    val response: HttpResponse =
        client.post(baseUrl + "mailExist.php") {
            url {
                parameters.append("email", emailValue)
            }
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    if(response.status.value == 401)
    {
        Toast.makeText(context, "ce mail est deja utilise par un autre utilisateur", Toast.LENGTH_SHORT).show()
        return
    }
    val response1: HttpResponse =
        client.post(baseUrl + "createUser.php") {
            url {
                parameters.append("name", nameValue)
                parameters.append("mail", emailValue)
                parameters.append("password", pswValue)
                parameters.append("organization", orga)
            }
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    if(response1.status.value == 401)
    {
        Toast.makeText(context, "ce mail est deja utilise par un autre utilisateur", Toast.LENGTH_SHORT).show()
        return
    } else
    {
        login(context, emailValue, pswValue)
        return
    }
}

fun isEmailValid(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

suspend fun sendCodeMail(code : String, mail : String)
{
    val response1: HttpResponse =
        client.post(baseUrl + "mailRecup.php") {
            url {
                parameters.append("code", code)
                parameters.append("mailTo", mail)
            }
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    val contenu = response1.bodyAsText()
    Log.d("API_RES", "Réponse du serveur : $contenu")
}

suspend fun changePassword(context: Context, mail: String, psw: String)
{
    val response1: HttpResponse =
        client.post(baseUrl + "changePassword.php") {
            url {
                parameters.append("email", mail)
                parameters.append("newpsw", psw)
            }
            header("X-Api-Key", BuildConfig.API_KEY)
        }
    val contenu = response1.bodyAsText()
    Log.d("API_RES", "Réponse du serveur : $contenu")
    val intent = Intent(context, LogInActivity::class.java)
    context.startActivity(intent)
}
