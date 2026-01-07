package com.josphat.myakibabenki.presentation.creategoal

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class CreateGoalUiState(
    val goalName: String = "",
    val goalCategory: String = "Travelling",
    val targetAmount: String = "",
    val targetDate: String = "",
    val targetDateMillis: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGoalNameValid: Boolean = true,
    val isTargetAmountValid: Boolean = true,
    val isTargetDateValid: Boolean = true,
    val goalCreated: Boolean = false,
    val successMessage: String? = null
) {
    val isFormValid: Boolean
        get() {
            val isNameValid = goalName.isNotBlank()
            val isAmountValid = targetAmount.isNotBlank() &&
                    targetAmount.toDoubleOrNull() != null &&
                    targetAmount.toDoubleOrNull()!! > 0

            val isDateValid = if (targetDateMillis != null) {
                val currentCalendar = Calendar.getInstance()
                currentCalendar.set(Calendar.HOUR_OF_DAY, 23)
                currentCalendar.set(Calendar.MINUTE, 59)
                currentCalendar.set(Calendar.SECOND, 59)
                currentCalendar.set(Calendar.MILLISECOND, 999)
                targetDateMillis > currentCalendar.timeInMillis
            } else {
                false
            }

            return isNameValid && isAmountValid && isDateValid
        }

    fun getFormattedAmount(): String {
        return targetAmount.toDoubleOrNull()?.let { amount ->
            String.format(Locale.US, "%.2f", amount)
        } ?: targetAmount
    }
}

enum class GoalCategory(val displayName: String) {
    TRAVELLING("Travelling"),
    FAMILY("Family"),
    SHOPPING("Shopping"),
    CAR("Car"),
    HOUSE("House"),
    FEES("Fees")
}