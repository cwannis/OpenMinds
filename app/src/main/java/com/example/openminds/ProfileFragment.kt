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
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.logoutText).setOnClickListener {
            logout(requireContext())
        }

        updatePP(view)
        updateText(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.allbadge)
        viewLifecycleOwner.lifecycleScope.launch {
            val sharedPref = requireContext().getSharedPreferences("OpenMindsPrefs", android.content.Context.MODE_PRIVATE)
            val id = sharedPref.getInt("USER_ID", 0)
            val mesBadge = getBadgesOfUser(requireContext(), id)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = BadgeAdapter(mesBadge)
        }
    }

    private fun updatePP(view: View) {
        val roundedimag = view.findViewById<ImageView>(R.id.roundedimage)
        var ppLink = arguments?.getString("pp")

        if (ppLink.isNullOrEmpty()) {
            ppLink = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgMC7WWfO9a2eZvPsSkxU4OjNTSgBqaXWjew&s"
        }
        Picasso.get()
            .load(ppLink)
            .into(roundedimag)

        Glide.with(requireContext())
            .load(ppLink)
            .into(roundedimag)
    }

    private fun updateText(view: View) {
        val profileName = view.findViewById<TextView>(R.id.ProfileName)
        val profileOrganization = view.findViewById<TextView>(R.id.ProfileOrganization)
        val name = arguments?.getString("nom")
        val orga = arguments?.getString("orga")
        if (!name.isNullOrEmpty() && !orga.isNullOrEmpty()) {
            profileName.text = name
            profileOrganization.text = orga
        }
    }
}