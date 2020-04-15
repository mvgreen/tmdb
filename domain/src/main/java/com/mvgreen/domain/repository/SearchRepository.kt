package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.entity.MovieContainer
import com.mvgreen.domain.entity.MovieData
import io.reactivex.Single

interface SearchRepository {

    fun loadGenres(): Single<Map<Int, GenreData>>

    fun search(query: String, page: Int): Single<MovieContainer>

}