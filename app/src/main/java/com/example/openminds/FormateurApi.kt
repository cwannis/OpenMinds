package com.example.openminds

import android.content.Context

suspend fun getFormateurSessions(context: Context, userId: Int): List<FormateurSession> {
    return dataWebRequete.makeRequest<FormateurSession>("getFormateurSessions.php", mapOf("user_id" to userId.toString()))
}

suspend fun getSessionParticipants(context: Context, sessionId: Int): List<Participant> {
    return dataWebRequete.makeRequest<Participant>("getSessionParticipants.php", mapOf("session_id" to sessionId.toString()))
}

suspend fun getFormateurStats(context: Context, userId: Int): List<FormateurStat> {
    return dataWebRequete.makeRequest<FormateurStat>("getFormateurStats.php", mapOf("user_id" to userId.toString()))
}
