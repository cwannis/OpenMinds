package com.example.openminds

data class Association(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val logoUrl: String? = "",
    val websiteUrl: String? = ""
)
