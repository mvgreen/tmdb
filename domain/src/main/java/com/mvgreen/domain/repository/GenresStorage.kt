package com.mvgreen.domain.repository

interface GenresStorage {

    fun saveGenresMap()

    fun getGenre(id: Int)

}