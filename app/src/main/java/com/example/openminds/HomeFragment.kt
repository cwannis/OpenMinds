package com.example.openminds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "") ?: ""
        val role = sharedPref.getString("USER_ROLE", "benevole") ?: "benevole"
        view.findViewById<TextView>(R.id.welcomeText).text = "Bienvenue, $userName"

        if (role == "formateur") {
            view.findViewById<Button>(R.id.btnBrowseFormations).text = "Mes Sessions"
            view.findViewById<Button>(R.id.btnBrowseFormations).setOnClickListener {
                startActivity(android.content.Intent(requireContext(), FormateurDashboardActivity::class.java))
            }
        } else {
            view.findViewById<Button>(R.id.btnBrowseFormations).setOnClickListener {
                (activity as? MainActivity)?.let {
                    it.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
                        .selectedItemId = R.id.nav_formations
                }
            }
        }

        view.findViewById<Button>(R.id.btnMyInscriptions).setOnClickListener {
            startActivity(android.content.Intent(requireContext(), MyInscriptionsActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnProgression).setOnClickListener {
            startActivity(android.content.Intent(requireContext(), ProgressionActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnMyBadges).setOnClickListener {
            startActivity(android.content.Intent(requireContext(), MyBadgesActivity::class.java))
        }
    }
}
