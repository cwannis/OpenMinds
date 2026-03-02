package com.example.openminds

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
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
import kotlinx.coroutines.launch
import android.util.Patterns
import android.widget.TextView
import androidx.core.view.PointerIconCompat.TYPE_TEXT

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
            signUp(this@RegisterActivity, nameValue, emailValue, pswValue, orga)
        }
    }

    fun showButton(view: View) {
        val input = findViewById<EditText>(R.id.pswEdit)
        val text = findViewById<TextView>(R.id.showButton)
        Log.i("PASSWORD HIDE", input.inputType.toString())
        if(input.inputType == InputType.TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD)
        {
            input.inputType = InputType.TYPE_CLASS_TEXT or TYPE_TEXT
            text.text = "hide"
        }
        else
        {
            input.inputType = InputType.TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            text.text = "show"
        }
    }
}