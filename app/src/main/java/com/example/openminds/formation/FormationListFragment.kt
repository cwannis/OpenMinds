package com.example.openminds.formation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.openminds.R
import com.example.openminds.utils.getAllFormations
import com.example.openminds.utils.getThematiques
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FormationListFragment : Fragment() {
    private lateinit var adapter: FormationListAdapter
    private val formations = mutableListOf<Formation>()
    private var selectedThematique = ""
    private var searchQuery = ""
    private var searchDebounceJob: Job? = null
    private var loadRequestId = 0

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
                        selectedThematique = if (pos == 0) "" else items[pos]
                        loadFormations(selectedThematique, searchQuery, progressBar)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                loadFormations("", "", progressBar)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                searchQuery = s?.toString()?.trim().orEmpty()
                searchDebounceJob?.cancel()
                searchDebounceJob = lifecycleScope.launch {
                    delay(300)
                    loadFormations(selectedThematique, searchQuery, progressBar)
                }
            }
        })

        searchBtn.setOnClickListener {
            searchQuery = searchInput.text.toString().trim()
            searchDebounceJob?.cancel()
            loadFormations(selectedThematique, searchQuery, progressBar)
        }
    }

    private fun loadFormations(thematique: String, search: String, progressBar: ProgressBar) {
        val requestId = ++loadRequestId
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val data = getAllFormations(requireContext(), thematique, search)
                if (requestId != loadRequestId) return@launch

                formations.clear()
                formations.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                if (requestId == loadRequestId) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}
