package com.example.openminds

import android.bluetooth.BluetoothHidDevice
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
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
                val client = HttpClient(Android)

                // Ta requête POST en 3 lignes
                val response: HttpResponse = client.post("http://10.0.2.2:63343/OpenMinds/phpFile/getUserData.php") {
                    // On ajoute tes paramètres
                    url {
                        parameters.append("email", "test@test.test")
                        parameters.append("password", "test")
                    }
                    // On ajoute ta clé API de sécurité [cite: 73]
                    header("X-Api-Key", BuildConfig.API_KEY)
                }

                val contenu = response.bodyAsText()
                Log.d("API_RES", "Réponse du serveur : $contenu")

            } catch (e: Exception) {
                Log.e("API_ERR", "Erreur : ${e.message}")
            }
        }
    }
}