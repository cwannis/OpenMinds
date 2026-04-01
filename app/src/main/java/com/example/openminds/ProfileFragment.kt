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

        val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
        val name = sharedPref.getString("USER_NAME", "") ?: ""
        val email = sharedPref.getString("USER_EMAIL", "") ?: ""
        val role = sharedPref.getString("USER_ROLE", "benevole") ?: "benevole"
        val ppUrl = sharedPref.getString("USER_PP", "")

        view.findViewById<TextView>(R.id.ProfileName).text = name
        view.findViewById<TextView>(R.id.ProfileEmail).text = email
        view.findViewById<TextView>(R.id.ProfileRole).text = when (role) {
            "admin" -> "Administrateur"
            "formateur" -> "Formateur"
            else -> "Benevole"
        }

        val imgUrl = if (ppUrl.isNullOrEmpty()) {
            getDefaultAvatar(name)
        } else ppUrl
        view.findViewById<ImageView>(R.id.roundedimage).load(imgUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_cercle_nav)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.allbadge)
        viewLifecycleOwner.lifecycleScope.launch {
            val userId = sharedPref.getInt("USER_ID", 0)
            val badges = getMyBadges(requireContext(), userId)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = BadgeAdapter(badges)
        }
    }

    private fun getDefaultAvatar(name: String): String {
        val avatars = listOf(
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&h=200&fit=crop",
            "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop",
            "https://images.unsplash.com/photo-1599566150163-29194dcaad36?w=200&h=200&fit=crop",
            "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=200&h=200&fit=crop",
            "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop"
        )
        return avatars[Math.abs(name.hashCode()) % avatars.size]
    }
}
