package com.lodrean.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.lodrean.model.Course
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SearchRepository
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load course by id sets content state`() = runTest(testDispatcher) {
        val course = Course(id = 42, title = "Android", summary = "Course", coverUrl = null)
        coEvery { repository.getCourseById(42) } returns course

        val savedStateHandle = SavedStateHandle().apply { set("courseId", 42L) }
        viewModel = DetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.course)
        assertEquals("Android", viewModel.uiState.value.course?.title)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `toggle favorite updates state and calls repository`() = runTest(testDispatcher) {
        val course = Course(id = 42, title = "Android", summary = "", coverUrl = null, isFavorite = false)
        coEvery { repository.getCourseById(42) } returns course

        val savedStateHandle = SavedStateHandle().apply { set("courseId", 42L) }
        viewModel = DetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.toggleFavorite()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.course?.isFavorite == true)
        coVerify { repository.addToFavorites(any()) }
    }
}
