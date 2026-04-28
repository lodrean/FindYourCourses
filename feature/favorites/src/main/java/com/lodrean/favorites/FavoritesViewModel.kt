package com.lodrean.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodrean.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavorites().collect { courses ->
                _uiState.update { it.copy(courses = courses, isLoading = false) }
            }
        }
    }

    fun removeFavorite(course: Course) {
        viewModelScope.launch {
            repository.removeFromFavorites(course)
        }
    }
}

data class FavoritesUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)
