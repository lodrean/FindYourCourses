package com.lodrean.network

import com.lodrean.network.dto.CourseDto
import com.lodrean.network.dto.CourseResponse
import com.lodrean.network.dto.SearchResultResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class StepikApiService {

    private val client = HttpClient(OkHttp) {
        defaultRequest {
            url("https://stepik.org/api/")
            headers.append("User-Agent", "FindYourCourses/1.0")
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
    }

    suspend fun searchCourses(query: String, page: Int = 1): SearchResultResponse {
        return client.get("search-results") {
            parameter("query", query)
            parameter("type", "course")
            parameter("page", page)
        }.body()
    }

    suspend fun getCoursesByIds(ids: List<Long>): CourseResponse {
        return client.get("courses") {
            ids.forEach { parameter("ids[]", it) }
        }.body()
    }

    suspend fun getCourseById(id: Long): CourseDto? {
        return client.get("courses") {
            parameter("ids[]", id)
        }.body<CourseResponse>().courses.firstOrNull()
    }
}
