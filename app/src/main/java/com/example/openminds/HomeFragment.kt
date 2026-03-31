package com.example.openminds

import android.content.Intent
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
        view.findViewById<TextView>(R.id.welcomeText).text = "Bienvenue, $userName"

        view.findViewById<Button>(R.id.btnBrowseFormations).setOnClickListener {
            startActivity(Intent(requireContext(), FormationListActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnMyInscriptions).setOnClickListener {
            startActivity(Intent(requireContext(), MyInscriptionsActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnProgression).setOnClickListener {
            startActivity(Intent(requireContext(), ProgressionActivity::class.java))
        }

        view.findViewById<Button>(R.id.btnMyBadges).setOnClickListener {
            startActivity(Intent(requireContext(), MyBadgesActivity::class.java))
        }
    }
}
