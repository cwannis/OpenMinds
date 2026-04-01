package com.example.openminds

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.logoutText).setOnClickListener { logout(this) }

        val sharedPref = getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
        val name = sharedPref.getString("USER_NAME", "") ?: ""
        val email = sharedPref.getString("USER_EMAIL", "") ?: ""
        val role = sharedPref.getString("USER_ROLE", "benevole") ?: "benevole"
        val ppUrl = sharedPref.getString("USER_PP", "")

        findViewById<TextView>(R.id.ProfileName).text = name
        findViewById<TextView>(R.id.ProfileEmail).text = email
        findViewById<TextView>(R.id.ProfileRole).text = when (role) {
            "admin" -> "Administrateur"
            "formateur" -> "Formateur"
            else -> "Benevole"
        }

        val imgUrl = if (ppUrl.isNullOrEmpty()) getDefaultAvatar(name) else ppUrl
        findViewById<ImageView>(R.id.roundedimage).load(imgUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_cercle_nav)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.allbadge)
        lifecycleScope.launch {
            val userId = sharedPref.getInt("USER_ID", 0)
            val badges = getMyBadges(this@ProfileActivity, userId)
            recyclerView.layoutManager = LinearLayoutManager(this@ProfileActivity)
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
