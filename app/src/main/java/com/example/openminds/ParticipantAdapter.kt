package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipantAdapter(
    private val participants: List<Participant>,
    private val onStatusChange: (Participant, String) -> Unit
) : RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvParticipantName)
        val email: TextView = view.findViewById(R.id.tvParticipantEmail)
        val status: TextView = view.findViewById(R.id.tvParticipantStatus)
        val btnPresent: Button = view.findViewById(R.id.btnPresent)
        val btnAbsent: Button = view.findViewById(R.id.btnAbsent)
        val btnTermine: Button = view.findViewById(R.id.btnTermine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = participants[position]
        holder.name.text = p.name
        holder.email.text = p.email
        holder.status.text = when (p.status) {
            "inscrit" -> "Inscrit"
            "present" -> "Present"
            "absent" -> "Absent"
            "termine" -> "Termine"
            else -> p.status
        }

        holder.btnPresent.setOnClickListener { onStatusChange(p, "present") }
        holder.btnAbsent.setOnClickListener { onStatusChange(p, "absent") }
        holder.btnTermine.setOnClickListener { onStatusChange(p, "termine") }
    }

    override fun getItemCount() = participants.size
}
