package com.josphat.myakibabenki.presentation.creategoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josphat.myakibabenki.domain.usecase.CreateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGoalUiState())
    val uiState: StateFlow<CreateGoalUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun updateGoalName(name: String) {
        val trimmedName = name.trim()
        _uiState.value = _uiState.value.copy(
            goalName = name,
            isGoalNameValid = trimmedName.isNotBlank() && trimmedName.length >= 2,
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
        // Allow only digits, decimal point, and handle formatting
        val filteredAmount = amount.filter { it.isDigit() || it == '.' }

        // Validate decimal format (max 2 decimal places)
        val isValidFormat = if (filteredAmount.contains('.')) {
            val parts = filteredAmount.split('.')
            parts.size <= 2 && (parts.size == 1 || parts[1].length <= 2)
        } else {
            true
        }

        if (isValidFormat) {
            val parsedAmount = filteredAmount.toDoubleOrNull()
            val isValid = filteredAmount.isNotBlank() &&
                    parsedAmount != null &&
                    parsedAmount > 0 &&
                    parsedAmount <= 999999999.99 // Reasonable upper limit

            _uiState.value = _uiState.value.copy(
                targetAmount = filteredAmount,
                isTargetAmountValid = isValid,
                error = null
            )
        }
    }

    fun updateTargetDate(dateMillis: Long) {
        // Get current time and set up end-of-day validation
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.HOUR_OF_DAY, 23)
        currentCalendar.set(Calendar.MINUTE, 59)
        currentCalendar.set(Calendar.SECOND, 59)
        currentCalendar.set(Calendar.MILLISECOND, 999)
        val todayEndMillis = currentCalendar.timeInMillis

        // Validate that the selected date is after today (not today or past)
        val isValidDate = dateMillis > todayEndMillis

        val formattedDate = try {
            dateFormatter.format(Date(dateMillis))
        } catch (e: Exception) {
            ""
        }

        _uiState.value = _uiState.value.copy(
            targetDate = formattedDate,
            targetDateMillis = dateMillis,
            isTargetDateValid = isValidDate,
            error = if (!isValidDate) "Target date must be tomorrow or later" else null
        )
    }

    fun createGoal() {
        val currentState = _uiState.value

        // Validate all fields before proceeding
        val validationErrors = mutableListOf<String>()

        val trimmedName = currentState.goalName.trim()
        if (trimmedName.isBlank() || trimmedName.length < 2) {
            validationErrors.add("Goal name must be at least 2 characters")
        }

        val amount = currentState.targetAmount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            validationErrors.add("Please enter a valid target amount")
        } else if (amount > 999999999.99) {
            validationErrors.add("Target amount is too large")
        }

        if (currentState.targetDateMillis == null) {
            validationErrors.add("Please select a target date")
        } else {
            // Use same validation logic as updateTargetDate
            val currentCalendar = Calendar.getInstance()
            currentCalendar.set(Calendar.HOUR_OF_DAY, 23)
            currentCalendar.set(Calendar.MINUTE, 59)
            currentCalendar.set(Calendar.SECOND, 59)
            currentCalendar.set(Calendar.MILLISECOND, 999)
            val todayEndMillis = currentCalendar.timeInMillis

            if (currentState.targetDateMillis <= todayEndMillis) {
                validationErrors.add("Target date must be tomorrow or later")
            }
        }

        if (validationErrors.isNotEmpty()) {
            val currentCalendar = Calendar.getInstance()
            currentCalendar.set(Calendar.HOUR_OF_DAY, 23)
            currentCalendar.set(Calendar.MINUTE, 59)
            currentCalendar.set(Calendar.SECOND, 59)
            currentCalendar.set(Calendar.MILLISECOND, 999)
            val todayEndMillis = currentCalendar.timeInMillis

            _uiState.value = currentState.copy(
                error = validationErrors.joinToString(". "),
                isGoalNameValid = trimmedName.isNotBlank() && trimmedName.length >= 2,
                isTargetAmountValid = amount != null && amount > 0 && amount <= 999999999.99,
                isTargetDateValid = currentState.targetDateMillis != null &&
                        currentState.targetDateMillis > todayEndMillis
            )
            return
        }

        _uiState.value = currentState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val result = createGoalUseCase(
                    name = trimmedName,
                    targetAmount = amount!!,
                    targetDate = currentState.targetDateMillis
                )

                result.fold(
                    onSuccess = { goalId ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = null,
                            goalCreated = true,
                            successMessage = "Goal saved successfully"
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

    fun onSuccessMessageDismissed() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun resetGoalCreatedState() {
        _uiState.value = _uiState.value.copy(goalCreated = false)
    }
}