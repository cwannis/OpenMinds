package com.example.openminds.loginLoout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.openminds.R
import com.example.openminds.utils.isEmailValid
import com.example.openminds.utils.signUp
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
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
    }

    fun goToLogIn(view: View) {
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }

    fun register(view: View) {
        val nameValue = findViewById<EditText>(R.id.editName).text.toString()
        val emailValue = findViewById<EditText>(R.id.editTextTextEmailAddress2).text.toString()
        val pswValue = findViewById<EditText>(R.id.pswEdit).text.toString()
        val readTerms = findViewById<CheckBox>(R.id.checkBox).isChecked

        if (nameValue.isEmpty() || emailValue.isEmpty() || pswValue.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        if (!readTerms) {
            Toast.makeText(this, "Vous devez accepter les conditions d'utilisation", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isEmailValid(emailValue)) {
            Toast.makeText(this, "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                signUp(this@RegisterActivity, nameValue, emailValue, pswValue)
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
