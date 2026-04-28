package com.lodrean.search

import com.lodrean.database.dao.CourseDao
import com.lodrean.database.entity.CourseEntity
import com.lodrean.model.Course
import com.lodrean.network.StepikApiService
import com.lodrean.network.dto.CourseDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val api: StepikApiService,
    private val courseDao: CourseDao,
) {

    suspend fun searchCourses(query: String, page: Int = 1): List<Course> {
        val searchResult = api.searchCourses(query, page)
        val courseIds = searchResult.searchResults.mapNotNull { it.course }
        if (courseIds.isEmpty()) return emptyList()
        val courseResponse = api.getCoursesByIds(courseIds)
        return courseResponse.courses.map { it.toCourse() }
    }

    suspend fun addToFavorites(course: Course) {
        courseDao.insert(course.toEntity())
    }

    suspend fun removeFromFavorites(course: Course) {
        courseDao.delete(course.toEntity())
    }

    fun getFavoritesFlow(): Flow<List<CourseEntity>> = courseDao.getAll()
}

fun CourseDto.toCourse(): Course = Course(
    id = id,
    title = title,
    summary = summary,
    coverUrl = cover,
    isFavorite = false,
)

fun Course.toEntity(): CourseEntity = CourseEntity(
    id = id,
    title = title,
    summary = summary,
    coverUrl = coverUrl,
    isFavorite = true,
)
