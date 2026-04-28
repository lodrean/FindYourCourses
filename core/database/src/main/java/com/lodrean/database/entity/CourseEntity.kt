package com.lodrean.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_courses")
data class CourseEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val summary: String,
    val coverUrl: String?,
    val isFavorite: Boolean = true,
)
