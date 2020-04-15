package com.mvgreen.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mvgreen.data.storage.db.entity.GenreEntity

@Database(
    entities = [GenreEntity::class],
    version = 1
)
abstract class GenreDb : RoomDatabase() {
    abstract fun taskDao(): GenreDao
}
