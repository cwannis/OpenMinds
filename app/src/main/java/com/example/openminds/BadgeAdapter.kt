package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class BadgeAdapter(private val liste: List<Badge>) : RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val titre: TextView = view.findViewById(R.id.tvTitreBadge)
        val desc: TextView = view.findViewById(R.id.tvDescriptionBadge)
        val time: TextView = view.findViewById(R.id.tvTimeBadge)
        val imageFormation : ImageView = view.findViewById(R.id.tvImageBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeAdapter.ViewHolder, position: Int) {
        val formation = liste[position]
        holder.titre.text = formation.titre
        holder.desc.text = formation.description
        holder.time.text = formatTimeAgo(formation.datePubli)

        holder.imageFormation.load(formation.imageUrl) {
            crossfade(true)
            placeholder(R.color.gray)
        }
    }

    override fun getItemCount() = liste.size

}