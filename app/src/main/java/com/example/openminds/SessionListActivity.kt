package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class SessionListActivity : AppCompatActivity() {
    private var formationId = 0
    private lateinit var adapter: SessionListAdapter
    private val sessions = mutableListOf<Session>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_session_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        formationId = intent.getIntExtra("FORMATION_ID", 0)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSessions)
        adapter = SessionListAdapter(sessions) { session ->
            if (isLogged(this)) {
                val userId = getUserId(this)
                inscrireToSession(userId, session.id)
            } else {
                Toast.makeText(this, "Connectez-vous pour vous inscrire", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadSessions()
    }

    private fun loadSessions() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                sessions.clear()
                sessions.addAll(getSessions(this@SessionListActivity, formationId))
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@SessionListActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun inscrireToSession(userId: Int, sessionId: Int) {
        lifecycleScope.launch {
            try {
                val status = inscrireSession(this@SessionListActivity, userId, sessionId)
                if (status == 201) {
                    Toast.makeText(this@SessionListActivity, "Inscription effectuee !", Toast.LENGTH_SHORT).show()
                } else if (status == 409) {
                    Toast.makeText(this@SessionListActivity, "Deja inscrit a cette session", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SessionListActivity, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SessionListActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
