package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.ktor.http.ContentType

class FormationAdapters(private val liste: List<Formation>) : RecyclerView.Adapter<FormationAdapters.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val titre: TextView = view.findViewById(R.id.tvTitre)
        val desc: TextView = view.findViewById(R.id.tvDescription)
        val time: TextView = view.findViewById(R.id.tvTime)
        val imageFormation : ImageView = view.findViewById(R.id.imageFormation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_formation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formation = liste[position]
        holder.titre.text = formation.titre
        holder.desc.text = formation.description
        holder.time.text = formatTimeAgo(formation.datePubli)

        holder.imageFormation.load(formation.imageUrl) {
            crossfade(true) // Ajoute une petite animation d'apparition douce
            placeholder(R.color.gray) // Couleur affichée pendant le chargement
        }
    }

    override fun getItemCount() = liste.size
}