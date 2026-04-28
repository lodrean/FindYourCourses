package com.lodrean.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultResponse(
    val meta: MetaDto,
    @SerialName("search-results")
    val searchResults: List<SearchResultDto>
)

@Serializable
data class SearchResultDto(
    val id: Long,
    val score: Double,
    val type: String,
    val course: Long? = null,
    val lesson: Long? = null,
)

@Serializable
data class MetaDto(
    val page: Int,
    @SerialName("has_next")
    val hasNext: Boolean,
    @SerialName("has_previous")
    val hasPrevious: Boolean,
)
