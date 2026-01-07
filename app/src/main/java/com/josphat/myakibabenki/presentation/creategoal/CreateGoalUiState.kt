package com.josphat.myakibabenki.presentation.creategoal

data class CreateGoalUiState(
    val goalName: String = "",
    val goalCategory: String = "Travelling",
    val targetAmount: String = "",
    val targetDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGoalNameValid: Boolean = true,
    val isTargetAmountValid: Boolean = true,
    val goalCreated: Boolean = false
) {
    val isFormValid: Boolean
        get() = goalName.isNotBlank() &&
                targetAmount.isNotBlank() &&
                targetAmount.toDoubleOrNull() != null &&
                targetAmount.toDoubleOrNull()!! > 0
}

enum class GoalCategory(val displayName: String) {
    TRAVELLING("Travelling"),
    EDUCATION("Education"),
    EMERGENCY("Emergency Fund"),
    HOUSING("Housing"),
    VEHICLE("Vehicle"),
    BUSINESS("Business"),
    INVESTMENT("Investment"),
    OTHER("Other")
}