package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.GenreData

interface GenresStorage {

    fun saveGenresMap(map: Map<Int, GenreData>)

    fun getGenre(id: Int): String?

}