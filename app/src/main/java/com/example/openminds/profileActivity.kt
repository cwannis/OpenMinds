package com.example.openminds

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

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
        // Load an image using Picasso library
        Picasso.get()
            .load("https://valoxy.org/blog/wp-content/uploads/2013/03/choisir-7-1.jpg")
            .into(roundedimag);

        // Load an image using Glide library
        Glide.with(getApplicationContext())
            .load("https://valoxy.org/blog/wp-content/uploads/2013/03/choisir-7-1.jpg")
            .into(roundedimag);
    }
}