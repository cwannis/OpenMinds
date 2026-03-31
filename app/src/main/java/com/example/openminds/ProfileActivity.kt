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

        findViewById<TextView>(R.id.btnMyBadges).setOnClickListener {
            startActivity(android.content.Intent(this, MyBadgesActivity::class.java))
        }

        val sharedPref = getSharedPreferences("OpenMindsPrefs", MODE_PRIVATE)
        val name = sharedPref.getString("USER_NAME", "") ?: ""
        val email = sharedPref.getString("USER_EMAIL", "") ?: ""
        val role = sharedPref.getString("USER_ROLE", "benevole") ?: "benevole"

        findViewById<TextView>(R.id.ProfileName).text = name
        findViewById<TextView>(R.id.ProfileEmail).text = email
        findViewById<TextView>(R.id.ProfileRole).text = when (role) {
            "admin" -> "Administrateur"
            "formateur" -> "Formateur"
            else -> "Benevole"
        }

        val ppUrl = sharedPref.getString("USER_PP", "")
        val imgUrl = if (ppUrl.isNullOrEmpty()) "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgMC7WWfO9a2eZPsSkxU4OjNTSgBqaXWjew&s" else ppUrl
        findViewById<ImageView>(R.id.roundedimage).load(imgUrl)

        val recyclerView = findViewById<RecyclerView>(R.id.allbadge)
        lifecycleScope.launch {
            val userId = sharedPref.getInt("USER_ID", 0)
            val badges = getMyBadges(this@ProfileActivity, userId)
            recyclerView.layoutManager = LinearLayoutManager(this@ProfileActivity)
            recyclerView.adapter = BadgeAdapter(badges)
        }
    }
}
