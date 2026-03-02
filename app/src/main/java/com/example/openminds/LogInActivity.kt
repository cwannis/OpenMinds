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
import androidx.core.content.edit

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

                login(this@LogInActivity, emailValue, passwordValue)

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