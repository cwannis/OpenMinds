package com.example.openminds.formation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.openminds.R
import com.example.openminds.utils.FormateurSession

class FormateurSessionAdapter(
    private val sessions: List<FormateurSession>,
    private val onClick: (FormateurSession) -> Unit
) : RecyclerView.Adapter<FormateurSessionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvSessionTitle)
        val date: TextView = view.findViewById(R.id.tvSessionDate)
        val location: TextView = view.findViewById(R.id.tvSessionLocation)
        val inscrits: TextView = view.findViewById(R.id.tvNbInscrits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_formateur_session, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = sessions[position]
        holder.title.text = s.formation_titre
        holder.date.text = s.date_debut
        holder.location.text = if (s.is_online == 1) "En ligne" else (s.location ?: "Non defini")
        holder.inscrits.text = "${s.nb_inscrits} inscrit(s)"
        holder.itemView.setOnClickListener { onClick(s) }
    }

    override fun getItemCount() = sessions.size
}
