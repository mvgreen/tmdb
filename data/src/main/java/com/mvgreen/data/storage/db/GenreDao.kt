@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.mvgreen.data.storage.db

import androidx.room.*
import com.mvgreen.data.storage.db.entity.GenreEntity

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres WHERE id=:id")
    fun getGenre(id: Int): GenreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(vararg task: GenreEntity)

    @Delete
    fun deleteGenres(vararg task: GenreEntity)

}