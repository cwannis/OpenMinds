package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var trendingAdapter: FormationListAdapter
    private val trendingFormations = mutableListOf<Formation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "") ?: ""
        view.findViewById<TextView>(R.id.welcomeText).text = "Bienvenue, $userName"

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTrending)
        trendingAdapter = FormationListAdapter(trendingFormations) { formation ->
            startActivity(Intent(requireContext(), FormationDetailActivity::class.java).apply {
                putExtra("FORMATION_ID", formation.id)
            })
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = trendingAdapter

        view.findViewById<Button>(R.id.btnMyInscriptions).setOnClickListener {
            startActivity(Intent(requireContext(), MyInscriptionsActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnProgression).setOnClickListener {
            startActivity(Intent(requireContext(), ProgressionActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnMyBadges).setOnClickListener {
            startActivity(Intent(requireContext(), MyBadgesActivity::class.java))
        }

        loadTrending()
    }

    private fun loadTrending() {
        lifecycleScope.launch {
            try {
                trendingFormations.clear()
                trendingFormations.addAll(getTrendingFormations(requireContext()))
                trendingAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
