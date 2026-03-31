package com.example.openminds

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {
    private var formationId = 0
    private var currentQuizIndex = 0
    private var quizzes = listOf<Quiz>()
    private val userAnswers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        formationId = intent.getIntExtra("FORMATION_ID", 0)
        loadQuiz()
    }

    private fun loadQuiz() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                quizzes = getQuiz(this@QuizActivity, formationId)
                if (quizzes.isNotEmpty() && quizzes[0].questions.isNotEmpty()) {
                    showQuestion(0)
                } else {
                    findViewById<TextView>(R.id.tvQuestion).text = "Aucun quiz disponible pour cette formation"
                    findViewById<Button>(R.id.btnNextQuestion).visibility = View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(this@QuizActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showQuestion(index: Int) {
        if (quizzes.isEmpty() || quizzes[0].questions.isEmpty()) return
        val q = quizzes[0].questions[index]
        findViewById<TextView>(R.id.tvQuizTitle).text = quizzes[0].titre
        findViewById<TextView>(R.id.tvQuestion).text = "${index + 1}/${quizzes[0].questions.size}: ${q.question}"
        findViewById<RadioButton>(R.id.radioA).text = "A) ${q.option_a}"
        findViewById<RadioButton>(R.id.radioB).text = "B) ${q.option_b}"
        findViewById<RadioButton>(R.id.radioC).text = "C) ${q.option_c}"
        findViewById<RadioButton>(R.id.radioD).text = "D) ${q.option_d}"

        val radioGroup = findViewById<android.widget.RadioGroup>(R.id.radioGroup)
        radioGroup.clearCheck()

        findViewById<Button>(R.id.btnNextQuestion).text = if (index == quizzes[0].questions.size - 1) "Terminer" else "Suivant"
        findViewById<Button>(R.id.btnNextQuestion).setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val answer = when (selectedId) {
                R.id.radioA -> "a"
                R.id.radioB -> "b"
                R.id.radioC -> "c"
                R.id.radioD -> "d"
                else -> ""
            }
            if (answer.isEmpty()) {
                Toast.makeText(this, "Selectionnez une reponse", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userAnswers.add(answer)
            if (index < quizzes[0].questions.size - 1) {
                showQuestion(index + 1)
            } else {
                submitAnswers()
            }
        }
    }

    private fun submitAnswers() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val userId = getUserId(this@QuizActivity)
                val quizId = quizzes[0].id
                val result = submitQuiz(this@QuizActivity, userId, quizId, userAnswers.joinToString(","))
                findViewById<TextView>(R.id.tvQuestion).text = "Resultat: ${result.score}/${result.total_questions} (${result.percentage}%)"
                findViewById<TextView>(R.id.tvQuizTitle).text = if (result.passed == 1) "Quiz reussi !" else "Quiz non reussi"
                findViewById<RadioGroup>(R.id.radioGroup).visibility = View.GONE
                findViewById<Button>(R.id.btnNextQuestion).visibility = View.GONE
                if (result.passed == 1) {
                    Toast.makeText(this@QuizActivity, "Badge obtenu !", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@QuizActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
