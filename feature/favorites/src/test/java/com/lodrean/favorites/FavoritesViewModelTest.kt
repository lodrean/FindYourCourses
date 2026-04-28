package com.lodrean.favorites

import app.cash.turbine.test
import com.lodrean.model.Course
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FavoritesRepository
    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getFavorites() } returns flowOf(
            listOf(
                Course(id = 1, title = "Kotlin", summary = "", coverUrl = null, isFavorite = true),
                Course(id = 2, title = "Java", summary = "", coverUrl = null, isFavorite = true),
            )
        )
        viewModel = FavoritesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `collect favorites updates ui state`() = runTest(testDispatcher) {
        advanceUntilIdle()
        assertEquals(2, viewModel.uiState.value.courses.size)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `remove favorite calls repository`() = runTest(testDispatcher) {
        val course = Course(id = 1, title = "Kotlin", summary = "", coverUrl = null, isFavorite = true)
        viewModel.removeFavorite(course)
        advanceUntilIdle()
        coVerify { repository.removeFromFavorites(course) }
    }
}
