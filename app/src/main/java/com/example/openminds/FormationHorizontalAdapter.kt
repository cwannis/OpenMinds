package com.example.openminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class FormationHorizontalAdapter(
    private val formations: List<Formation>,
    private val onClick: (Formation) -> Unit
) : RecyclerView.Adapter<FormationHorizontalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvFormationTitle)
        val thematique: TextView = view.findViewById(R.id.tvThematique)
        val image: ImageView = view.findViewById(R.id.ivFormationImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_formation_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val f = formations[position]
        holder.title.text = f.titre
        holder.thematique.text = f.thematique
        if (!f.imageUrl.isNullOrEmpty()) {
            holder.image.load(f.imageUrl) { crossfade(true); placeholder(R.drawable.ic_cercle_nav) }
        }
        holder.itemView.setOnClickListener { onClick(f) }
    }

    override fun getItemCount() = formations.size
}
