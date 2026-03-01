package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import io.ktor.serialization.gson.*

class LogInActivity : AppCompatActivity() {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun login(view: View) {
        lifecycleScope.launch {
            try {
                val emailInput = findViewById<EditText>(R.id.editTextTextEmailAddress2)
                val pswInput = findViewById<EditText>(R.id.mdpText)
                val emailValue = emailInput.text.toString()
                val passwordValue = pswInput.text.toString()

                if (emailValue.isEmpty() || passwordValue.isEmpty()) {
                    Toast.makeText(this@LogInActivity, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Ta requête POST en 3 lignes
                val response: HttpResponse = client.post("http://10.0.2.2/openMinds/phpFile/getUserData.php") {
                    // On ajoute tes paramètres
                    url {
                        parameters.append("email", emailValue)
                        parameters.append("password", passwordValue)
                    }
                    // On ajoute ta clé API de sécurité [cite: 73]
                    header("X-Api-Key", BuildConfig.API_KEY)
                }

                val contenu = response.bodyAsText()
                Log.d("API_RES", "Réponse du serveur : $contenu")

                if(response.status.value == 200)
                {
                    val users = response.body<List<User>>();
                    if(users.isNotEmpty()) {
                        val user = users[0]
                        Log.i("API_RES", "utilisateur " + user.name)
                        Toast.makeText(this@LogInActivity, "Bienvenue ${user.name}", Toast.LENGTH_SHORT).show()
                        val sharedPref = getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)

                        // 2. ENREGISTRE LES INFOS
                        with(sharedPref.edit()) {
                            putInt("USER_ID", user.id)
                            putString("USER_NAME", user.name)
                            putBoolean("IS_LOGGED", true)
                            apply()
                        }
                    }
                } else
                {
                    Toast.makeText(this@LogInActivity, "le mail ou le mot de passe est incorect", Toast.LENGTH_SHORT).show()
                    return@launch
                }

            } catch (e: Exception) {
                Log.e("API_ERR", "Erreur : ${e.message}")
                Toast.makeText(this@LogInActivity, "Une erreur est survenu avec le serve", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goToRegister(view: View) {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }


}