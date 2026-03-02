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
import kotlinx.coroutines.launch

class ResetPswCode : AppCompatActivity() {

    private var code : String = "000000"
    private var email : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_psw_code)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        code = (1..6).map { (0..9).random() }.joinToString("")
        Log.i("CODE RECUP", code)
        lifecycleScope.launch {
            val mail = intent.extras?.getString("mail")

            if (!mail.isNullOrEmpty()) {
                email = mail
                sendCodeMail(code, mail)
            } else {
                Log.e("MAIL_ERR", "L'adresse mail est manquante ou vide")
            }
        }
    }

    fun back(view: View) {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun valider(view: View)
    {
        val entercode = findViewById<EditText>(R.id.validationCodeEdit).text.toString()
        if(entercode != code)
        {
            Toast.makeText(this, "code invalide", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, ResetPassword::class.java)
        intent.putExtra("mail", email)
        startActivity(intent)
        finish()
    }
}