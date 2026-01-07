package com.josphat.myakibabenki.presentation.home

import com.josphat.myakibabenki.domain.model.Goal

data class HomeUiState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val greeting: String = "Hello There!",
    val subtitle: String = "It's a good day to save"
) {
    val hasGoals: Boolean get() = goals.isNotEmpty()
}