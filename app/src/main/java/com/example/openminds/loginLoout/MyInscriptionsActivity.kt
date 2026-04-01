package com.example.openminds.loginLoout

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openminds.R
import com.example.openminds.utils.getMyInscriptions
import com.example.openminds.utils.getUserId
import com.example.openminds.utils.isLogged
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class MyInscriptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_inscriptions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (!isLogged(this)) {
            Toast.makeText(this, "Connectez-vous", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewInscriptions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val userId = getUserId(this@MyInscriptionsActivity)
                val inscriptions = getMyInscriptions(this@MyInscriptionsActivity, userId)
                recyclerView.adapter = InscriptionListAdapter(inscriptions)
            } catch (e: Exception) {
                Toast.makeText(this@MyInscriptionsActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
