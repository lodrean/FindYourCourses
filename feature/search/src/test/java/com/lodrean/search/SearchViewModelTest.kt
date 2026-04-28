package com.lodrean.search

import com.lodrean.model.Course
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getFavoritesFlow() } returns flowOf(emptyList())
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search query returns content after debounce`() = runTest(testDispatcher) {
        val courses = listOf(
            Course(id = 1, title = "Kotlin", summary = "", coverUrl = null),
            Course(id = 2, title = "Java", summary = "", coverUrl = null),
        )
        coEvery { repository.searchCourses("test", 1) } returns SearchResult(courses, false)

        viewModel.onQueryChanged("test")
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.courses.size)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.hasNext)
    }

    @Test
    fun `toggle favorite calls repository and updates state`() = runTest(testDispatcher) {
        val course = Course(id = 1, title = "Kotlin", summary = "", coverUrl = null, isFavorite = false)
        coEvery { repository.addToFavorites(any()) } returns Unit

        viewModel.toggleFavorite(course)
        advanceUntilIdle()

        coVerify { repository.addToFavorites(course) }
    }

    @Test
    fun `load next page appends courses`() = runTest(testDispatcher) {
        val page1 = listOf(Course(id = 1, title = "A", summary = "", coverUrl = null))
        val page2 = listOf(Course(id = 2, title = "B", summary = "", coverUrl = null))
        coEvery { repository.searchCourses("q", 1) } returns SearchResult(page1, true)
        coEvery { repository.searchCourses("q", 2) } returns SearchResult(page2, false)

        viewModel.onQueryChanged("q")
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.courses.size)
        assertTrue(viewModel.uiState.value.hasNext)

        viewModel.loadNextPage()
        advanceUntilIdle()
        assertEquals(2, viewModel.uiState.value.courses.size)
        assertFalse(viewModel.uiState.value.hasNext)
    }
}
