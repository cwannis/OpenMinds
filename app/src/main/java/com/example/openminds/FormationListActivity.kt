package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FormationListActivity : AppCompatActivity() {
    private lateinit var adapter: FormationListAdapter
    private val formations = mutableListOf<Formation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formation_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFormations)
        adapter = FormationListAdapter(formations) { formation ->
            startActivity(Intent(this, FormationDetailActivity::class.java).apply {
                putExtra("FORMATION_ID", formation.id)
            })
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val spinnerCategory = findViewById<Spinner>(R.id.spinnerThematique)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val searchInput = findViewById<EditText>(R.id.editTextSearch)
        val searchBtn = findViewById<Button>(R.id.btnSearch)

        lifecycleScope.launch {
            try {
                val themes = getThematiques(this@FormationListActivity)
                val items = listOf("Toutes") + themes
                val arr = ArrayAdapter(this@FormationListActivity, android.R.layout.simple_spinner_item, items)
                arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory.adapter = arr

                spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                        val theme = if (pos == 0) "" else items[pos]
                        loadFormations(theme, "", progressBar)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                loadFormations("", "", progressBar)
            } catch (e: Exception) {
                Toast.makeText(this@FormationListActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        searchBtn.setOnClickListener {
            val query = searchInput.text.toString().trim()
            loadFormations("", query, progressBar)
        }
    }

    private fun loadFormations(thematique: String, search: String, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                formations.clear()
                formations.addAll(getAllFormations(this@FormationListActivity, thematique, search))
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@FormationListActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
