package com.example.openminds.formation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.openminds.R

class ProgressionAdapter(
    private val formations: List<Formation>
) : RecyclerView.Adapter<ProgressionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvProgressionTitle)
        val thematique: TextView = view.findViewById(R.id.tvProgressionThematique)
        val quizStatus: TextView = view.findViewById(R.id.tvProgressionQuiz)
        val sessionsStatus: TextView = view.findViewById(R.id.tvProgressionSessions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progression, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val f = formations[position]
        holder.title.text = f.titre
        holder.thematique.text = f.thematique
        holder.quizStatus.text = if (f.quiz_passed == 1) "Quiz reussi (${f.quiz_score}/${f.quiz_total})" else if (f.quiz_total > 0) "Quiz non reussi (${f.quiz_score}/${f.quiz_total})" else "Quiz non passe"
        holder.sessionsStatus.text = "${f.sessions_inscrites} session(s) inscrite(s)"
        holder.itemView.contentDescription = "Progression: ${f.titre} - ${holder.quizStatus.text}"
    }

    override fun getItemCount() = formations.size
}
