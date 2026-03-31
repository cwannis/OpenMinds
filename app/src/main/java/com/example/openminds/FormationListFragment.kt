package com.example.openminds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FormationListFragment : Fragment() {
    private lateinit var adapter: FormationListAdapter
    private val formations = mutableListOf<Formation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_formation_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewFormations)
        adapter = FormationListAdapter(formations) { formation ->
            startActivity(Intent(requireContext(), FormationDetailActivity::class.java).apply {
                putExtra("FORMATION_ID", formation.id)
            })
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val spinnerCategory = view.findViewById<Spinner>(R.id.spinnerThematique)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val searchInput = view.findViewById<EditText>(R.id.editTextSearch)
        val searchBtn = view.findViewById<Button>(R.id.btnSearch)

        lifecycleScope.launch {
            try {
                val themes = getThematiques(requireContext())
                val items = listOf("Toutes") + themes
                val arr = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
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
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
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
                formations.addAll(getAllFormations(requireContext(), thematique, search))
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
