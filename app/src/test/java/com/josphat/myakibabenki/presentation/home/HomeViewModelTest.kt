package com.josphat.myakibabenki.presentation.home

import com.josphat.myakibabenki.domain.model.Goal
import com.josphat.myakibabenki.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class HomeViewModelTest {

    @Mock
    private lateinit var repository: SavingsRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `initial state should show empty goals and not loading`() = runTest {
        // Given
        `when`(repository.observeAllGoals()).thenReturn(flowOf(emptyList()))

        // When
        viewModel = HomeViewModel(repository)

        // Then
        val uiState = viewModel.uiState.value
        assertEquals(emptyList(), uiState.goals)
        assertFalse(uiState.hasGoals)
    }

    @Test
    fun `clearError should clear error message`() = runTest {
        // Given
        `when`(repository.observeAllGoals()).thenReturn(flowOf(emptyList()))
        viewModel = HomeViewModel(repository)

        // When
        viewModel.clearError()

        // Then
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }
}