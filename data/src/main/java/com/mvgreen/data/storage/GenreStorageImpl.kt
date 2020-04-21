package com.mvgreen.data.storage

import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.repository.GenreStorage
import javax.inject.Inject

class GenreStorageImpl @Inject constructor() : GenreStorage {

    private val genres = HashMap<Int, GenreData>()

    override fun saveGenresMap(map: Map<Int, GenreData>) {
        genres.putAll(map)
    }

    override fun getGenre(id: Int): String? {
        return genres[id]?.name
    }

}