package com.example.openminds.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openminds.R
import com.example.openminds.admin.AdminDashboardActivity
import com.example.openminds.formation.FormateurDashboardActivity
import com.example.openminds.formation.Formation
import com.example.openminds.formation.FormationDetailActivity
import com.example.openminds.formation.FormationHorizontalAdapter
import com.example.openminds.utils.getAllFormations
import com.example.openminds.utils.getTrendingFormations
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var trendingAdapter: FormationHorizontalAdapter
    private val trendingFormations = mutableListOf<Formation>()
    private lateinit var suggestionsAdapter: FormationHorizontalAdapter
    private val suggestionsFormations = mutableListOf<Formation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "") ?: ""
        val role = sharedPref.getString("USER_ROLE", "benevole") ?: "benevole"
        view.findViewById<TextView>(R.id.welcomeText).text = "Bienvenue, $userName"

        val btnDashboard = view.findViewById<Button>(R.id.btnDashboard)
        if (role == "formateur") {
            btnDashboard.text = "Mes Sessions"
            btnDashboard.visibility = View.VISIBLE
            btnDashboard.setOnClickListener {
                startActivity(Intent(requireContext(), FormateurDashboardActivity::class.java))
            }
        } else if (role == "admin") {
            btnDashboard.text = "Tableau de bord"
            btnDashboard.visibility = View.VISIBLE
            btnDashboard.setOnClickListener {
                startActivity(Intent(requireContext(), AdminDashboardActivity::class.java))
            }
        }

        trendingAdapter = FormationHorizontalAdapter(trendingFormations) { openFormation(it) }
        view.findViewById<RecyclerView>(R.id.recyclerViewTrending).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingAdapter
        }

        suggestionsAdapter = FormationHorizontalAdapter(suggestionsFormations) { openFormation(it) }
        view.findViewById<RecyclerView>(R.id.recyclerViewSuggestions).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = suggestionsAdapter
        }

        view.findViewById<Button>(R.id.btnSeeAll).setOnClickListener {
            (activity as? MainActivity)?.let {
                it.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                    .selectedItemId = R.id.nav_formations
            }
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
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}