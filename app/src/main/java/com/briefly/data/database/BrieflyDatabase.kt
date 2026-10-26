package com.briefly.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.briefly.data.dao.ArticleDao
import com.briefly.data.dto.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class BrieflyDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}