package com.example.openminds

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        val mesFormations = listOf(
            Formation("lucas feron", "apprendre a connaitre lucas feron", "2m ago"),
            Formation("seigneur cazer", "une longue histoire lui", "15m ago"),
            Formation("jsp", "bon la javoue jai plus d'inspi", "35m ago")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FormationAdapters(mesFormations)
    }

    fun logOut(view: View) {
        logout(this)
    }
}