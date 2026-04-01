package com.example.openminds.loginLoout

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
import com.example.openminds.Main.MainActivity
import com.example.openminds.R
import com.example.openminds.utils.isEmailValid
import com.example.openminds.utils.isLogged
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
        if (isLogged(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
                com.example.openminds.utils.login(this@LogInActivity, emailValue, passwordValue)
            } catch (e: Exception) {
                Log.e("API_ERR", "Erreur : ${e.message}")
                Toast.makeText(this@LogInActivity, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goToRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }



    fun goToResetPswCode(view: View) {
        val emailInput = findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
        if (emailInput.isNotEmpty() && isEmailValid(emailInput)) {
            val intent = Intent(this, ResetPswCode::class.java)
            intent.putExtra("mail", emailInput)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Entrez un email valide", Toast.LENGTH_SHORT).show()
        }
    }
}
