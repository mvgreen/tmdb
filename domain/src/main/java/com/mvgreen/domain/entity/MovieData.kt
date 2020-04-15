package com.mvgreen.domain.entity

import org.joda.time.DateTime

data class MovieData(
    val id: Int,
    val posterLink: String?,
    val title: String,
    val originalTitle: String,
    val releaseDate: DateTime?,
    val genres: List<GenreData>,
    val averageVote: Float?,
    val voteCount: Int?,
    val runtime: Int
)

data class MovieContainer(
    val currentPage: Int,
    val pagesTotal: Int,
    val movies: MutableList<MovieData>
)