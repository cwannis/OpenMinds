package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class BadgeAdapter(
    private val badges: List<Badge>
) : RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    class BadgeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.tvImageBadge)
        val titre: TextView = view.findViewById(R.id.tvTitreBadge)
        val description: TextView = view.findViewById(R.id.tvDescriptionBadge)
        val time: TextView = view.findViewById(R.id.tvTimeBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = badges[position]
        holder.titre.text = badge.titre
        holder.description.text = badge.description
        holder.image.load(badge.imageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_cercle_nav)
        }
        if (badge.dateObtention.isNotEmpty()) {
            holder.time.text = badge.dateObtention.substring(0, 10)
        } else {
            holder.time.text = ""
        }
        holder.itemView.contentDescription = "${badge.titre}: ${badge.description}"
    }

    override fun getItemCount() = badges.size
}
