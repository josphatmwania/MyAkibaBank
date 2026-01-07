package com.josphat.myakibabenki.domain.usecase

import com.josphat.myakibabenki.domain.model.Goal
import com.josphat.myakibabenki.domain.repository.SavingsRepository
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateGoalUseCase @Inject constructor(
    private val repository: SavingsRepository
) {
    suspend operator fun invoke(
        name: String,
        targetAmount: Double,
        targetDate: Long? = null
    ): Result<Long> {
        return try {
            val roundedAmount = String.format(Locale("en", "KE"), "%.2f", targetAmount).toDouble()
            val goal = Goal(
                name = name,
                targetAmount = roundedAmount,
                targetDate = targetDate,
                createdAt = System.currentTimeMillis()
            )
            val goalId = repository.createGoal(goal)
            Result.success(goalId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}