package com.lodrean.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lodrean.database.dao.CourseDao
import com.lodrean.database.entity.CourseEntity

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
