package com.example.openminds

data class Quiz(
    val id: Int,
    val formation_id: Int,
    val titre: String,
    val passing_score: Int = 60,
    val questions: List<QuizQuestion> = emptyList()
)

data class QuizQuestion(
    val id: Int,
    val quiz_id: Int,
    val question: String,
    val option_a: String,
    val option_b: String,
    val option_c: String,
    val option_d: String,
    val correct_answer: String = ""
)
