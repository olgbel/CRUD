package ru.netology.dto

data class PostRequestDto(
    val id: Long,
    val author: String,
    val content: String? = null,
    val address: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val youtubeURL: String? = null
)