package com.josphat.myakibabenki.presentation.creategoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josphat.myakibabenki.domain.usecase.CreateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGoalUiState())
    val uiState: StateFlow<CreateGoalUiState> = _uiState.asStateFlow()

    fun updateGoalName(name: String) {
        _uiState.value = _uiState.value.copy(
            goalName = name,
            isGoalNameValid = name.isNotBlank(),
            error = null
        )
    }

    fun updateGoalCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            goalCategory = category,
            error = null
        )
    }

    fun updateTargetAmount(amount: String) {
        // Allow only digits, decimal point, and backspace
        val filteredAmount = amount.filter { it.isDigit() || it == '.' }

        // Validate decimal format (max 2 decimal places)
        val isValidFormat = if (filteredAmount.contains('.')) {
            val parts = filteredAmount.split('.')
            parts.size <= 2 && (parts.size == 1 || parts[1].length <= 2)
        } else {
            true
        }

        if (isValidFormat) {
            val isValid = filteredAmount.isNotBlank() &&
                    filteredAmount.toDoubleOrNull() != null &&
                    filteredAmount.toDoubleOrNull()!! > 0

            _uiState.value = _uiState.value.copy(
                targetAmount = filteredAmount,
                isTargetAmountValid = isValid,
                error = null
            )
        }
    }

    fun updateTargetDate(date: String) {
        _uiState.value = _uiState.value.copy(
            targetDate = date,
            error = null
        )
    }

    fun createGoal() {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            _uiState.value = currentState.copy(
                error = "Please fill in all required fields correctly",
                isGoalNameValid = currentState.goalName.isNotBlank(),
                isTargetAmountValid = currentState.targetAmount.isNotBlank() &&
                        currentState.targetAmount.toDoubleOrNull() != null &&
                        currentState.targetAmount.toDoubleOrNull()!! > 0
            )
            return
        }

        _uiState.value = currentState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val result = createGoalUseCase(
                    name = currentState.goalName.trim(),
                    targetAmount = currentState.targetAmount.toDouble()
                    // Note: targetDate will be added later when calendar functionality is implemented
                )

                result.fold(
                    onSuccess = {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = null,
                            goalCreated = true
                        )
                    },
                    onFailure = { throwable ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to create goal"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun onErrorDismissed() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetGoalCreatedState() {
        _uiState.value = _uiState.value.copy(goalCreated = false)
    }
}