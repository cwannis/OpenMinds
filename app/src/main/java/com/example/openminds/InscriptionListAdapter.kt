package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InscriptionListAdapter(
    private val inscriptions: List<Inscription>
) : RecyclerView.Adapter<InscriptionListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val formation: TextView = view.findViewById(R.id.tvInscriptionFormation)
        val date: TextView = view.findViewById(R.id.tvInscriptionDate)
        val status: TextView = view.findViewById(R.id.tvInscriptionStatus)
        val location: TextView = view.findViewById(R.id.tvInscriptionLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inscription, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = inscriptions[position]
        holder.formation.text = i.formation_titre
        holder.date.text = i.date_debut
        holder.status.text = when (i.status) {
            "inscrit" -> "Inscrit"
            "present" -> "Present"
            "absent" -> "Absent"
            "termine" -> "Termine"
            else -> i.status
        }
        holder.location.text = if (i.is_online == 1) "En ligne" else (i.location ?: "Non defini")
        holder.itemView.contentDescription = "Inscription a ${i.formation_titre} - ${holder.status.text}"
    }

    override fun getItemCount() = inscriptions.size
}
