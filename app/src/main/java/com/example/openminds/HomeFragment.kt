package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var trendingAdapter: FormationHorizontalAdapter
    private val trendingFormations = mutableListOf<Formation>()
    private lateinit var suggestionsAdapter: FormationHorizontalAdapter
    private val suggestionsFormations = mutableListOf<Formation>()
    private lateinit var allAdapter: FormationListAdapter
    private val allFormations = mutableListOf<Formation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "") ?: ""
        view.findViewById<TextView>(R.id.welcomeText).text = "Bienvenue, $userName"

        trendingAdapter = FormationHorizontalAdapter(trendingFormations) { openFormation(it) }
        view.findViewById<RecyclerView>(R.id.recyclerViewTrending).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingAdapter
        }

        suggestionsAdapter = FormationHorizontalAdapter(suggestionsFormations) { openFormation(it) }
        view.findViewById<RecyclerView>(R.id.recyclerViewSuggestions).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = suggestionsAdapter
        }

        allAdapter = FormationListAdapter(allFormations) { openFormation(it) }
        view.findViewById<RecyclerView>(R.id.recyclerViewAll).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = allAdapter
        }

        loadData()
    }

    private fun openFormation(formation: Formation) {
        startActivity(Intent(requireContext(), FormationDetailActivity::class.java).apply {
            putExtra("FORMATION_ID", formation.id)
        })
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val all = getAllFormations(requireContext())
                val trending = getTrendingFormations(requireContext())

                trendingFormations.clear()
                trendingFormations.addAll(trending.take(4))
                trendingAdapter.notifyDataSetChanged()

                val trendingIds = trending.map { it.id }.toSet()
                val others = all.filter { it.id !in trendingIds }.shuffled()
                suggestionsFormations.clear()
                suggestionsFormations.addAll(others.take(4))
                suggestionsAdapter.notifyDataSetChanged()

                allFormations.clear()
                allFormations.addAll(all)
                allAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
