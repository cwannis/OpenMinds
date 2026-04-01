package com.example.openminds.formation

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
import com.example.openminds.formation.ParticipantAdapter
import com.example.openminds.R
import com.example.openminds.utils.Participant
import com.example.openminds.utils.getSessionParticipants
import com.example.openminds.utils.getUserId
import com.example.openminds.utils.updateParticipantStatus
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class SessionParticipantsActivity : AppCompatActivity() {
    private var sessionId = 0
    private lateinit var adapter: ParticipantAdapter
    private val participants = mutableListOf<Participant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_session_participants)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        sessionId = intent.getIntExtra("SESSION_ID", 0)
        val sessionTitle = intent.getStringExtra("SESSION_TITLE") ?: ""
        findViewById<TextView>(R.id.tvSessionTitle).text = sessionTitle

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewParticipants)
        adapter = ParticipantAdapter(participants) { participant, newStatus ->
            updateStatus(participant.user_id, newStatus)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadParticipants()
    }

    private fun loadParticipants() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                participants.clear()
                participants.addAll(
                    getSessionParticipants(
                        this@SessionParticipantsActivity,
                        sessionId
                    )
                )
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@SessionParticipantsActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun updateStatus(participantId: Int, newStatus: String) {
        lifecycleScope.launch {
            try {
                val formateurId = getUserId(this@SessionParticipantsActivity)
                val status = updateParticipantStatus(
                    this@SessionParticipantsActivity,
                    formateurId,
                    participantId,
                    sessionId,
                    newStatus
                )
                if (status == 200) {
                    val idx = participants.indexOfFirst { it.user_id == participantId }
                    if (idx >= 0) {
                        participants[idx] = participants[idx].copy(status = newStatus)
                        adapter.notifyItemChanged(idx)
                    }
                    val label = when (newStatus) {
                        "present" -> "Present"
                        "absent" -> "Absent"
                        "termine" -> "Termine"
                        else -> newStatus
                    }
                    Toast.makeText(this@SessionParticipantsActivity, "Statut mis a jour: $label", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SessionParticipantsActivity, "Erreur lors de la mise a jour", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SessionParticipantsActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
