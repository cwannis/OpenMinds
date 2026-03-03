package com.example.openminds

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class profileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val roundedimag :(ImageView) = findViewById(R.id.roundedimage);
        var ppLink=  intent.extras?.getString("pp")

        if (ppLink.isNullOrEmpty()) {
            ppLink = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgMC7WWfO9a2eZvPsSkxU4OjNTSgBqaXWjew&s"
        }
        Picasso.get()
            .load(ppLink)
            .into(roundedimag);

        Glide.with(getApplicationContext())
            .load(ppLink)
            .into(roundedimag);
    }
}