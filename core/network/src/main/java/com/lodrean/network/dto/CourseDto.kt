package com.lodrean.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse(
    val meta: MetaDto,
    val courses: List<CourseDto>
)

@Serializable
data class CourseDto(
    val id: Long,
    val title: String,
    val summary: String = "",
    val cover: String? = null,
    val authors: List<Long> = emptyList(),
)
