package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminInscriptionAdapter(
    private val inscriptions: List<AdminInscription>
) : RecyclerView.Adapter<AdminInscriptionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvAdminName)
        val formation: TextView = view.findViewById(R.id.tvAdminFormation)
        val status: TextView = view.findViewById(R.id.tvAdminStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_inscription, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = inscriptions[position]
        holder.name.text = i.name
        holder.formation.text = i.formation_titre
        holder.status.text = when (i.status) {
            "inscrit" -> "Inscrit"
            "present" -> "Present"
            "absent" -> "Absent"
            "termine" -> "Termine"
            else -> i.status
        }
    }

    override fun getItemCount() = inscriptions.size
}
