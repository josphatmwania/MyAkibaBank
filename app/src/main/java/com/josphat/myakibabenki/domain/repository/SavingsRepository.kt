package com.josphat.myakibabenki.domain.repository

import com.josphat.myakibabenki.domain.model.Contribution
import com.josphat.myakibabenki.domain.model.Goal
import com.josphat.myakibabenki.domain.model.GoalWithProgress
import kotlinx.coroutines.flow.Flow

interface SavingsRepository {

    // Goal operations
    fun observeAllGoals(): Flow<List<Goal>>
    suspend fun getGoalById(goalId: Long): Goal?
    suspend fun createGoal(goal: Goal): Long
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goalId: Long)

    // Contribution operations
    fun observeContributionsByGoal(goalId: Long): Flow<List<Contribution>>
    suspend fun addContribution(contribution: Contribution): Long
    suspend fun getGoalWithProgress(goalId: Long): GoalWithProgress?

    // Verification operations
    suspend fun getGoalsCount(): Int
    suspend fun getContributionsCount(): Int
    suspend fun verifyGoalExists(goalId: Long): Boolean
}