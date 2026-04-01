package com.example.openminds

import android.os.Bundle
import android.view.View
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

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<android.widget.Button>(R.id.btnGoHome).setOnClickListener {
            startActivity(android.content.Intent(this, MainActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewRecent)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadDashboard()
    }

    private fun toInt(value: Any?): Long {
        return (value as? Number)?.toLong() ?: 0L
    }

    private fun loadDashboard() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val userId = getUserId(this@AdminDashboardActivity)
                val result = getAdminDashboard(this@AdminDashboardActivity, userId)

                val stats = result["stats"] as? Map<String, Any>
                if (stats != null) {
                    findViewById<TextView>(R.id.tvBenevoles).text = "Benevoles: " + toInt(stats["nb_benevoles"])
                    findViewById<TextView>(R.id.tvFormateurs).text = "Formateurs: " + toInt(stats["nb_formateurs"])
                    findViewById<TextView>(R.id.tvFormations).text = "Formations: " + toInt(stats["nb_formations"])
                    findViewById<TextView>(R.id.tvSessions).text = "Sessions: " + toInt(stats["nb_sessions"])
                    findViewById<TextView>(R.id.tvInscriptions).text = "Inscriptions: " + toInt(stats["nb_inscriptions"])
                    findViewById<TextView>(R.id.tvQuizPasses).text = "Quiz passes: " + toInt(stats["nb_quiz_passes"])
                    findViewById<TextView>(R.id.tvQuizReussis).text = "Quiz reussis: " + toInt(stats["nb_quiz_reussis"])
                    findViewById<TextView>(R.id.tvTauxReussite).text = "Taux reussite: " + toInt(stats["taux_reussite"]) + "%"
                }

                val recent = result["recent_inscriptions"] as? List<Map<String, Any>>
                if (recent != null) {
                    val adapter = AdminInscriptionAdapter(recent.map {
                        AdminInscription(
                            name = it["name"] as? String ?: "",
                            formation_titre = it["formation_titre"] as? String ?: "",
                            status = it["status"] as? String ?: "",
                            inscrit_le = it["inscrit_le"] as? String ?: ""
                        )
                    })
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminDashboardActivity, "Erreur: " + e.message, Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
