package com.example.openminds

import java.sql.Date

data class Formation(
                    val id: Int,
                    val titre: String,
                    val description : String,
                    val datePubli : Long,
                    val imageUrl : String
                    )

fun formatTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val years = days / 365

    return when {
        years > 0 -> "${years}y ago"
        days > 0 -> "${days}d ago"
        hours > 0 -> "${hours}h ago"
        minutes > 0 -> "${minutes}m ago"
        else -> "Just now"
    }
}