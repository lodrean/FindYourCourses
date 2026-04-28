package com.lodrean.model

data class Course(
    val id: Long,
    val title: String,
    val summary: String,
    val coverUrl: String?,
    val isFavorite: Boolean = false,
)
