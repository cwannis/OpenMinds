package com.example.openminds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.logoutText).setOnClickListener {
            logout(requireContext())
        }

        view.findViewById<TextView>(R.id.btnMyBadges).setOnClickListener {
            startActivity(android.content.Intent(requireContext(), MyBadgesActivity::class.java))
        }

        val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
        val name = sharedPref.getString("USER_NAME", "") ?: ""
        val email = sharedPref.getString("USER_EMAIL", "") ?: ""
        val role = sharedPref.getString("USER_ROLE", "benevole") ?: "benevole"

        view.findViewById<TextView>(R.id.ProfileName).text = name
        view.findViewById<TextView>(R.id.ProfileEmail).text = email
        view.findViewById<TextView>(R.id.ProfileRole).text = when (role) {
            "admin" -> "Administrateur"
            "formateur" -> "Formateur"
            else -> "Benevole"
        }

        val ppUrl = sharedPref.getString("USER_PP", "")
        val imgUrl = if (ppUrl.isNullOrEmpty()) {
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgMC7WWfO9a2eZPsSkxU4OjNTSgBqaXWjew&s"
        } else ppUrl
        view.findViewById<ImageView>(R.id.roundedimage).load(imgUrl)

        val recyclerView = view.findViewById<RecyclerView>(R.id.allbadge)
        viewLifecycleOwner.lifecycleScope.launch {
            val userId = sharedPref.getInt("USER_ID", 0)
            val badges = getMyBadges(requireContext(), userId)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = BadgeAdapter(badges)
        }
    }
}
