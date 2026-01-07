package com.josphat.myakibabenki.data.repository

import android.util.Log
import com.josphat.myakibabenki.data.local.dao.ContributionDao
import com.josphat.myakibabenki.data.local.dao.GoalDao
import com.josphat.myakibabenki.data.mapper.ContributionEntityMapper
import com.josphat.myakibabenki.data.mapper.GoalEntityMapper
import com.josphat.myakibabenki.domain.model.Contribution
import com.josphat.myakibabenki.domain.model.Goal
import com.josphat.myakibabenki.domain.model.GoalWithProgress
import com.josphat.myakibabenki.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SavingsRepository"

@Singleton
class SavingsRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao,
    private val contributionDao: ContributionDao,
    private val goalMapper: GoalEntityMapper,
    private val contributionMapper: ContributionEntityMapper
) : SavingsRepository {

    // Goal operations
    override fun observeAllGoals(): Flow<List<Goal>> {
        return goalDao.observeAllGoals().map { goalMapper.toDomainList(it) }
    }

    override suspend fun getGoalById(goalId: Long): Goal? {
        return try {
            goalDao.getGoalById(goalId)?.let { goalMapper.toDomain(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting goal by id: $goalId", e)
            null
        }
    }

    override suspend fun createGoal(goal: Goal): Long {
        return try {
            val entity = goalMapper.toEntity(goal)
            val goalId = goalDao.insertGoal(entity)
            Log.d(TAG, "Goal created successfully with id: $goalId, name: ${goal.name}")
            
            // Verify the goal was saved
            val savedGoal = goalDao.getGoalById(goalId)
            if (savedGoal != null) {
                Log.d(TAG, "Goal verified in database: id=$goalId, name=${savedGoal.name}")
            } else {
                Log.e(TAG, "Warning: Goal was inserted but not found in database: id=$goalId")
            }
            
            goalId
        } catch (e: Exception) {
            Log.e(TAG, "Error creating goal: ${goal.name}", e)
            throw e
        }
    }

    override suspend fun updateGoal(goal: Goal) {
        try {
            val entity = goalMapper.toEntity(goal)
            goalDao.updateGoal(entity)
            Log.d(TAG, "Goal updated successfully: id=${goal.id}, name=${goal.name}")
            
            // Verify the update
            val updatedGoal = goalDao.getGoalById(goal.id)
            if (updatedGoal != null) {
                Log.d(TAG, "Goal update verified: id=${goal.id}, name=${updatedGoal.name}")
            } else {
                Log.e(TAG, "Warning: Goal update failed - goal not found: id=${goal.id}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating goal: id=${goal.id}", e)
            throw e
        }
    }

    override suspend fun deleteGoal(goalId: Long) {
        try {
            goalDao.deleteGoal(goalId)
            Log.d(TAG, "Goal deleted successfully: id=$goalId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting goal: id=$goalId", e)
            throw e
        }
    }

    // Contribution operations
    override fun observeContributionsByGoal(goalId: Long): Flow<List<Contribution>> {
        return contributionDao.observeContributionsByGoal(goalId)
            .map { contributionMapper.toDomainList(it) }
    }

    override suspend fun addContribution(contribution: Contribution): Long {
        return try {
            val entity = contributionMapper.toEntity(contribution)
            val contributionId = contributionDao.insertContribution(entity)
            Log.d(TAG, "Contribution added successfully: id=$contributionId, goalId=${contribution.goalId}, amount=${contribution.amount}")
            contributionId
        } catch (e: Exception) {
            Log.e(TAG, "Error adding contribution: goalId=${contribution.goalId}", e)
            throw e
        }
    }

    override suspend fun getGoalWithProgress(goalId: Long): GoalWithProgress? {
        return try {
            val goal = getGoalById(goalId) ?: return null
            val contributions = contributionDao.getContributionsByGoal(goalId)
                .map { contributionMapper.toDomain(it) }

            GoalWithProgress(goal, contributions)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting goal with progress: goalId=$goalId", e)
            null
        }
    }

    // Verification operations
    override suspend fun getGoalsCount(): Int {
        return try {
            val count = goalDao.getGoalsCount()
            Log.d(TAG, "Total goals in database: $count")
            count
        } catch (e: Exception) {
            Log.e(TAG, "Error getting goals count", e)
            0
        }
    }

    override suspend fun getContributionsCount(): Int {
        return try {
            val count = contributionDao.getContributionsCount()
            Log.d(TAG, "Total contributions in database: $count")
            count
        } catch (e: Exception) {
            Log.e(TAG, "Error getting contributions count", e)
            0
        }
    }

    override suspend fun verifyGoalExists(goalId: Long): Boolean {
        return try {
            val exists = goalDao.getGoalById(goalId) != null
            Log.d(TAG, "Goal verification: id=$goalId, exists=$exists")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying goal existence: id=$goalId", e)
            false
        }
    }
}