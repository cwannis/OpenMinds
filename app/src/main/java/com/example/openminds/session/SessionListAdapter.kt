package com.example.openminds.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.openminds.R

class SessionListAdapter(
    private val sessions: List<Session>,
    private var myInscriptions: Set<Int>,
    private val onInscrire: (Session) -> Unit,
    private val onDesinscrire: (Session) -> Unit
) : RecyclerView.Adapter<SessionListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.tvSessionDate)
        val location: TextView = view.findViewById(R.id.tvSessionLocation)
        val formateur: TextView = view.findViewById(R.id.tvSessionFormateur)
        val type: TextView = view.findViewById(R.id.tvSessionType)
        val btnAction: Button = view.findViewById(R.id.btnInscrire)
    }

    fun updateInscriptions(inscriptions: Set<Int>) {
        myInscriptions = inscriptions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = sessions[position]
        holder.date.text = s.date_debut
        holder.location.text = if (s.is_online == 1) "En ligne" else (s.location ?: "Non defini")
        holder.formateur.text = s.formateur_name ?: "Non assigne"
        holder.type.text = if (s.is_online == 1) "En ligne" else "En presentiel"

        if (myInscriptions.contains(s.id)) {
            holder.btnAction.text = "Se desinscrire"
            holder.btnAction.setOnClickListener { onDesinscrire(s) }
        } else {
            holder.btnAction.text = "S'inscrire"
            holder.btnAction.setOnClickListener { onInscrire(s) }
        }
        holder.itemView.contentDescription = "Session le ${s.date_debut} - ${holder.location.text}"
    }

    override fun getItemCount() = sessions.size
}
