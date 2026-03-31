package com.example.openminds

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {
    private var formationId = 0
    private var formationTitle = ""
    private var quizzes = listOf<Quiz>()
    private val userAnswers = mutableListOf<String>()
    private var quizCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        formationId = intent.getIntExtra("FORMATION_ID", 0)
        formationTitle = intent.getStringExtra("FORMATION_TITLE") ?: ""
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

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.clearCheck()
        radioGroup.visibility = View.VISIBLE

        findViewById<Button>(R.id.btnNextQuestion).text = if (index == quizzes[0].questions.size - 1) "Terminer" else "Suivant"
        findViewById<Button>(R.id.btnNextQuestion).visibility = View.VISIBLE
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
        if (!isLogged(this)) {
            Toast.makeText(this, "Connectez-vous pour passer le quiz", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val userId = getUserId(this@QuizActivity)
                val quizId = quizzes[0].id
                val result = submitQuiz(this@QuizActivity, userId, quizId, userAnswers.joinToString(","))

                showResults(result.score, result.total_questions, result.percentage, result.passed == 1)
            } catch (e: Exception) {
                Toast.makeText(this@QuizActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showResults(score: Int, total: Int, percentage: Int, passed: Boolean) {
        quizCompleted = true
        val container = findViewById<LinearLayout>(R.id.quizContainer)
        container.removeAllViews()

        val titleText = TextView(this).apply {
            text = if (passed) "Quiz reussi !" else "Quiz non reussi"
            textSize = 22f
            setTextColor(if (passed) Color.parseColor("#4CAF50") else Color.parseColor("#F44336"))
            setPadding(0, 0, 0, 16)
        }
        container.addView(titleText)

        val scoreText = TextView(this).apply {
            text = "Score: $score/$total ($percentage%)"
            textSize = 18f
            setPadding(0, 0, 0, 24)
        }
        container.addView(scoreText)

        val questions = quizzes[0].questions
        for (i in questions.indices) {
            val q = questions[i]
            val userAnswer = if (i < userAnswers.size) userAnswers[i] else "?"
            val isCorrect = userAnswer == q.correct_answer

            val questionText = TextView(this).apply {
                text = "${i + 1}. ${q.question}"
                textSize = 16f
                setTextColor(Color.BLACK)
                setPadding(0, 8, 0, 4)
            }
            container.addView(questionText)

            val answerText = TextView(this).apply {
                val userLabel = "Votre reponse: ${userAnswer.uppercase()}"
                val correctLabel = "Bonne reponse: ${q.correct_answer.uppercase()}"
                text = if (isCorrect) "$userLabel ✓" else "$userLabel ✗ ($correctLabel)"
                textSize = 14f
                setTextColor(if (isCorrect) Color.parseColor("#4CAF50") else Color.parseColor("#F44336"))
                setPadding(0, 0, 0, 12)
            }
            container.addView(answerText)
        }

        val buttonsLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 24, 0, 0)
        }

        val btnHome = Button(this).apply {
            text = "Retour a l'accueil"
            setOnClickListener {
                startActivity(Intent(this@QuizActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }
        }
        buttonsLayout.addView(btnHome)

        val btnFormation = Button(this).apply {
            text = "Voir la formation"
            setOnClickListener {
                startActivity(Intent(this@QuizActivity, FormationDetailActivity::class.java).apply {
                    putExtra("FORMATION_ID", formationId)
                })
                finish()
            }
        }
        buttonsLayout.addView(btnFormation)

        container.addView(buttonsLayout)
    }
}
