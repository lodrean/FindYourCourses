package com.lodrean.favorites

import com.lodrean.database.dao.CourseDao
import com.lodrean.database.entity.CourseEntity
import com.lodrean.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val courseDao: CourseDao,
) {

    fun getFavorites(): Flow<List<Course>> = courseDao.getAll().map { list ->
        list.map { it.toCourse() }
    }

    suspend fun removeFromFavorites(course: Course) {
        courseDao.delete(course.toEntity())
    }
}

private fun CourseEntity.toCourse(): Course = Course(
    id = id,
    title = title,
    summary = summary,
    coverUrl = coverUrl,
    isFavorite = true,
)

private fun Course.toEntity(): CourseEntity = CourseEntity(
    id = id,
    title = title,
    summary = summary,
    coverUrl = coverUrl,
    isFavorite = true,
)
