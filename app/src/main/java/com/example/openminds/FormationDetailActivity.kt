package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch

class FormationDetailActivity : AppCompatActivity() {
    private var formationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formation_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        formationId = intent.getIntExtra("FORMATION_ID", 0)
        if (formationId == 0) {
            Toast.makeText(this, "Formation introuvable", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<Button>(R.id.btnViewSessions).setOnClickListener {
            startActivity(Intent(this, SessionListActivity::class.java).apply {
                putExtra("FORMATION_ID", formationId)
            })
        }

        findViewById<Button>(R.id.btnTakeQuiz).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java).apply {
                putExtra("FORMATION_ID", formationId)
            })
        }

        loadFormation()
    }

    private fun loadFormation() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val f = getFormation(this@FormationDetailActivity, formationId)
                findViewById<TextView>(R.id.tvFormationTitle).text = f.titre
                findViewById<TextView>(R.id.tvThematique).text = f.thematique
                findViewById<TextView>(R.id.tvDescription).text = f.description
                findViewById<TextView>(R.id.tvDuration).text = "Duree: ${f.duration_minutes} min"
                findViewById<TextView>(R.id.tvType).text = when (f.type) {
                    "online" -> "En ligne"
                    "in-person" -> "En presentiel"
                    "both" -> "En ligne et en presentiel"
                    else -> f.type
                }

                if (!f.imageUrl.isNullOrEmpty()) {
                    findViewById<ImageView>(R.id.ivFormationImage).load(f.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_cercle_nav)
                    }
                }

                if (!f.content.isNullOrEmpty()) {
                    findViewById<TextView>(R.id.tvContent).text = f.content.replace(Regex("<[^>]*>"), "")
                }
            } catch (e: Exception) {
                Toast.makeText(this@FormationDetailActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
