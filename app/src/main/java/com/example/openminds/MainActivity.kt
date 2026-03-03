package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(!isLogged(this))
        {
            logout(this)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.monRecyclerView)
         lifecycleScope.launch {
             val mesFormations = getAllForm(this@MainActivity)
             recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
             recyclerView.adapter = FormationAdapters(mesFormations)
         }
    }

    fun logOut(view: View) {
        logout(this)
    }

    fun goToProfile(view: View) {
        val intent = Intent(this, profileActivity::class.java)
        val sharedPref = getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
        val id =  sharedPref.getInt("USER_ID", 0)
        lifecycleScope.launch {
            val user = getUserData(this@MainActivity, id)
            intent.putExtra("nom", user.name)
            intent.putExtra("orga", user.organization)
            intent.putExtra("pp", user.ppLink)
            startActivity(intent)
            finish()
        }
    }
}