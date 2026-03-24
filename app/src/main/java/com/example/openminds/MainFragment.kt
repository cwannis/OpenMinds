package com.example.openminds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isLogged(requireContext())) {
            logout(requireContext())
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.monRecyclerView)
        viewLifecycleOwner.lifecycleScope.launch {
            val mesFormations = getAllForm(requireContext())
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = FormationAdapters(mesFormations)
        }

        view.findViewById<TextView>(R.id.btnLogOut).setOnClickListener {
            logout(requireContext())
        }

        view.findViewById<TextView>(R.id.textViewFilter).setOnClickListener {
            // Logic to go to profile or filter. 
            // In the original MainActivity, goToProfile was triggered by this view (id: textViewFilter)
            val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
            val id = sharedPref.getInt("USER_ID", 0)
            viewLifecycleOwner.lifecycleScope.launch {
                val user = getUserData(requireContext(), id)
                val bundle = Bundle().apply {
                    putString("nom", user.name)
                    putString("orga", user.organization)
                    putString("pp", user.ppLink)
                }
                // Here we would typically navigate to ProfileFragment
                // For now, I'll assume we use parentFragmentManager or a NavController if available
                val profileFragment = ProfileFragment().apply {
                    arguments = bundle
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, profileFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}