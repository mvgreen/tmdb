package com.mvgreen.domain.entity

data class MovieData(
    val posterLink: String,
    val title: String,
    val originalTitle: String,
    val year: String,
    val genres: List<GenreData>,
    val averageVote: Float,
    val voteCount: Int,
    val runtime: Int
)