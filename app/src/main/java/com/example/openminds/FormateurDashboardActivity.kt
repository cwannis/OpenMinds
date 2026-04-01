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
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class FormateurDashboardActivity : AppCompatActivity() {
    private lateinit var sessionsAdapter: FormateurSessionAdapter
    private val sessions = mutableListOf<FormateurSession>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formateur_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<Button>(R.id.btnGoHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSessions)
        sessionsAdapter = FormateurSessionAdapter(sessions) { session ->
            startActivity(Intent(this, SessionParticipantsActivity::class.java).apply {
                putExtra("SESSION_ID", session.id)
                putExtra("SESSION_TITLE", session.formation_titre)
            })
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = sessionsAdapter

        loadSessions()
    }

    private fun loadSessions() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val userId = getUserId(this@FormateurDashboardActivity)
                sessions.clear()
                sessions.addAll(getFormateurSessions(this@FormateurDashboardActivity, userId))
                sessionsAdapter.notifyDataSetChanged()

                val stats = getFormateurStats(this@FormateurDashboardActivity, userId)
                val totalInscrits = stats.sumOf { it.nb_inscrits }
                val totalQuiz = stats.sumOf { it.nb_quiz_passes }
                val totalReussis = stats.sumOf { it.nb_quiz_reussis }
                findViewById<TextView>(R.id.tvStatsSummary).text =
                    "Total inscrits: $totalInscrits | Quiz passes: $totalQuiz | Taux reussite: ${if (totalQuiz > 0) totalReussis * 100 / totalQuiz else 0}%"
            } catch (e: Exception) {
                Toast.makeText(this@FormateurDashboardActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
