package com.example.quantomproject.homepage.data

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)