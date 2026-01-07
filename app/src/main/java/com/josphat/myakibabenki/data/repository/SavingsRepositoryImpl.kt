package com.josphat.myakibabenki.data.repository

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
        return goalDao.getGoalById(goalId)?.let { goalMapper.toDomain(it) }
    }

    override suspend fun createGoal(goal: Goal): Long {
        return goalDao.insertGoal(goalMapper.toEntity(goal))
    }

    override suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goalMapper.toEntity(goal))
    }

    override suspend fun deleteGoal(goalId: Long) {
        goalDao.deleteGoal(goalId)
    }

    // Contribution operations
    override fun observeContributionsByGoal(goalId: Long): Flow<List<Contribution>> {
        return contributionDao.observeContributionsByGoal(goalId)
            .map { contributionMapper.toDomainList(it) }
    }

    override suspend fun addContribution(contribution: Contribution): Long {
        return contributionDao.insertContribution(contributionMapper.toEntity(contribution))
    }

    override suspend fun getGoalWithProgress(goalId: Long): GoalWithProgress? {
        val goal = getGoalById(goalId) ?: return null
        val contributions = contributionDao.getContributionsByGoal(goalId)
            .map { contributionMapper.toDomain(it) }

        return GoalWithProgress(goal, contributions)
    }
}