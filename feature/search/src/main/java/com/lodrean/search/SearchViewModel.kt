package com.lodrean.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lodrean.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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
                        flow { emit(emptyList<Course>() to false to emptySet<Long>()) }
                    } else {
                        flow {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                            try {
                                val result = repository.searchCourses(q, page = 1)
                                val favIds = favoriteIdsFlow.first()
                                emit(result.courses to result.hasNext to favIds)
                            } catch (e: Exception) {
                                _uiState.update { it.copy(isLoading = false, error = e.message) }
                                emit(emptyList<Course>() to false to emptySet<Long>())
                            }
                        }
                    }
                }
                .collect { (coursesAndHasNext, favIds) ->
                    val (courses, hasNext) = coursesAndHasNext
                    _uiState.update {
                        it.copy(
                            courses = courses.map { course ->
                                course.copy(isFavorite = favIds.contains(course.id))
                            },
                            hasNext = hasNext,
                            currentPage = 1,
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

    fun loadNextPage() {
        val currentState = _uiState.value
        if (currentState.isLoadingNextPage || !currentState.hasNext) return
        val q = _query.value
        if (q.isBlank()) return
        val nextPage = currentState.currentPage + 1

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingNextPage = true) }
            try {
                val result = repository.searchCourses(q, nextPage)
                val favIds = favoriteIdsFlow.first()
                _uiState.update { state ->
                    val newCourses = state.courses + result.courses.map { course ->
                        course.copy(isFavorite = favIds.contains(course.id))
                    }
                    state.copy(
                        courses = newCourses,
                        hasNext = result.hasNext,
                        currentPage = nextPage,
                        isLoadingNextPage = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingNextPage = false, error = e.message) }
            }
        }
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            if (course.isFavorite) {
                repository.removeFromFavorites(course)
            } else {
                repository.addToFavorites(course)
            }
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
    val isLoadingNextPage: Boolean = false,
    val error: String? = null,
    val hasNext: Boolean = false,
    val currentPage: Int = 1,
)
