package com.lodrean.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodrean.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val favoriteIdsFlow = repository.getFavoritesFlow()
        .map { list -> list.map { it.id }.toSet() }

    init {
        viewModelScope.launch {
            _query
                .debounce(300)
                .flatMapLatest { q ->
                    if (q.isBlank()) {
                        flow { emit(emptyList<Course>() to emptySet<Long>()) }
                    } else {
                        flow {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                            try {
                                val courses = repository.searchCourses(q)
                                val favIds = favoriteIdsFlow.first()
                                emit(courses to favIds)
                            } catch (e: Exception) {
                                _uiState.update { it.copy(isLoading = false, error = e.message) }
                                emit(emptyList<Course>() to emptySet<Long>())
                            }
                        }
                    }
                }
                .collect { (courses, favIds) ->
                    _uiState.update {
                        it.copy(
                            courses = courses.map { course ->
                                course.copy(isFavorite = favIds.contains(course.id))
                            },
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            if (course.isFavorite) {
                repository.removeFromFavorites(course)
            } else {
                repository.addToFavorites(course)
            }
            // optimistic update
            _uiState.update { state ->
                state.copy(
                    courses = state.courses.map {
                        if (it.id == course.id) it.copy(isFavorite = !course.isFavorite) else it
                    }
                )
            }
        }
    }
}

data class SearchUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
