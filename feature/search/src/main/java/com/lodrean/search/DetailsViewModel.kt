package com.lodrean.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodrean.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: SearchRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val courseId: Long = savedStateHandle.get<Long>("courseId") ?: -1L

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState

    init {
        if (courseId != -1L) {
            loadCourse()
        }
    }

    private fun loadCourse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val course = repository.getCourseById(courseId)
                if (course != null) {
                    _uiState.update { it.copy(course = course, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Курс не найден") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun toggleFavorite() {
        val course = _uiState.value.course ?: return
        val newFavoriteState = !course.isFavorite
        _uiState.update { it.copy(course = course.copy(isFavorite = newFavoriteState)) }
        viewModelScope.launch {
            try {
                if (newFavoriteState) {
                    repository.addToFavorites(course.copy(isFavorite = true))
                } else {
                    repository.removeFromFavorites(course.copy(isFavorite = false))
                }
            } catch (e: Exception) {
                // rollback on error
                _uiState.update { it.copy(course = course.copy(isFavorite = !newFavoriteState), error = e.message) }
            }
        }
    }
}

data class DetailsUiState(
    val course: Course? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
