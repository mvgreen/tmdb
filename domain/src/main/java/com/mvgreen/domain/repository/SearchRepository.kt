package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.entity.MovieData
import io.reactivex.Single

interface SearchRepository {

    fun loadGenres(): Single<HashMap<Int, GenreData>>

    fun search(query: String, page: Int): Single<List<MovieData>>

}