package com.mvgreen.data.storage

import com.mvgreen.data.storage.db.GenreDao
import com.mvgreen.data.storage.db.entity.GenreEntity
import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.repository.GenresStorage
import javax.inject.Inject

class GenreStorageImpl @Inject constructor(
    private val genreDao: GenreDao
) : GenresStorage {

    override fun saveGenresMap(map: Map<Int, GenreData>) {
        genreDao.insertGenres(*map.values.map { GenreEntity(it.id, it.name) }.toTypedArray())
    }

    override fun getGenre(id: Int): String? {
        return genreDao.getGenre(id)?.name
    }

}