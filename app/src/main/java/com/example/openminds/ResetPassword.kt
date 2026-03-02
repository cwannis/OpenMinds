package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ResetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun back(view: View) {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun valider(view: View) {
        val npsw = findViewById<EditText>(R.id.validationCodeEdit).text.toString()
        if(npsw.isEmpty())
        {
            Toast.makeText(this, "veuillez remplire le champ", Toast.LENGTH_SHORT).show()
            return
        }
        var mail = intent.extras?.getString("mail")

        if (!mail.isNullOrEmpty()) {
            lifecycleScope.launch {
                changePassword(this@ResetPassword, mail, npsw)
            }
        } else {
            Log.e("MAIL_ERR", "L'adresse mail est manquante ou vide")
        }
    }
}