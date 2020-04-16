package com.mvgreen.domain.entity

import org.joda.time.DateTime

data class MovieData(
    val id: Int,
    val posterLink: String? = null,
    val title: String? = null,
    val originalTitle: String? = null,
    val releaseDate: DateTime? = null,
    val genres: List<GenreData>,
    val averageVote: Float? = null,
    val voteCount: Int? = null,
    val runtime: Int? = null
)

data class MovieContainer(
    val currentPage: Int,
    val pagesTotal: Int,
    val movies: MutableList<MovieData>
)