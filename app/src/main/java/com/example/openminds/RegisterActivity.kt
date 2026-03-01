package com.example.openminds

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.launch
import android.util.Patterns

class RegisterActivity : AppCompatActivity() {
    private val client = HttpClient(Android)


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegister = findViewById<Button>(R.id.signUpButton)
        btnRegister.setOnClickListener {
            register()
        }
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun goToLogIn(view: View) {
        val i = Intent(this, LogInActivity::class.java)
        startActivity(i)
        finish()
    }

    fun register() {
        val nameValue = findViewById<EditText>(R.id.editName).text.toString()
        val emailValue = findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
        val pswValue = findViewById<EditText>(R.id.pswEdit).text.toString()
        val orga = findViewById<EditText>(R.id.Organisation).text.toString()
        val readTherme = findViewById<CheckBox>(R.id.checkBox).isChecked

        if(nameValue.isEmpty() || emailValue.isEmpty() || pswValue.isEmpty() || orga.isEmpty())
        {
            Toast.makeText(this@RegisterActivity, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        if(!readTherme)
        {
            Toast.makeText(this@RegisterActivity, "Vous devez acceptez les therme d'utilisation", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isEmailValid(emailValue))
        {
            Toast.makeText(this@RegisterActivity, "Veillez donner un mail valide", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val response: HttpResponse =
                client.post("http://10.0.2.2/openMinds/phpFile/mailExist.php") {
                    url {
                        parameters.append("email", emailValue)
                    }
                    header("X-Api-Key", BuildConfig.API_KEY)
                }
            if(response.status.value == 401)
            {
                Toast.makeText(this@RegisterActivity, "ce mail est deja utilise par un autre utilisateur", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val response1: HttpResponse =
                client.post("http://10.0.2.2/openMinds/phpFile/createUser.php") {
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
                    Toast.makeText(this@RegisterActivity, "ce mail est deja utilise par un autre utilisateur", Toast.LENGTH_SHORT).show()
                    return@launch
                } else
                {
                    Toast.makeText(this@RegisterActivity, "utilisateur cree", Toast.LENGTH_SHORT).show()
                    return@launch
                }
        }
    }
}