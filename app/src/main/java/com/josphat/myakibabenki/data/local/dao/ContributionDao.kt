package com.josphat.myakibabenki.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.josphat.myakibabenki.data.local.entity.ContributionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContributionDao {

    @Query("SELECT * FROM contributions WHERE goalId = :goalId ORDER BY date DESC")
    fun observeContributionsByGoal(goalId: Long): Flow<List<ContributionEntity>>

    @Query("SELECT * FROM contributions WHERE goalId = :goalId ORDER BY date DESC")
    suspend fun getContributionsByGoal(goalId: Long): List<ContributionEntity>

    @Query("SELECT SUM(amount) FROM contributions WHERE goalId = :goalId")
    suspend fun getTotalSavedForGoal(goalId: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: ContributionEntity): Long

    @Update
    suspend fun updateContribution(contribution: ContributionEntity)

    @Query("DELETE FROM contributions WHERE id = :contributionId")
    suspend fun deleteContribution(contributionId: Long)

    @Query("DELETE FROM contributions WHERE goalId = :goalId")
    suspend fun deleteContributionsByGoal(goalId: Long)

    @Query("DELETE FROM contributions")
    suspend fun deleteAllContributions()
}