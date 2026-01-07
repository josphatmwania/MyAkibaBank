package com.josphat.myakibabenki.di

import android.content.Context
import androidx.room.Room
import com.josphat.myakibabenki.data.local.dao.ContributionDao
import com.josphat.myakibabenki.data.local.dao.GoalDao
import com.josphat.myakibabenki.data.local.database.SavingsDatabase
import com.josphat.myakibabenki.data.repository.SavingsRepositoryImpl
import com.josphat.myakibabenki.domain.repository.SavingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSavingsDatabase(
        @ApplicationContext context: Context
    ): SavingsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SavingsDatabase::class.java,
            "savings_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGoalDao(database: SavingsDatabase): GoalDao {
        return database.goalDao()
    }

    @Provides
    fun provideContributionDao(database: SavingsDatabase): ContributionDao {
        return database.contributionDao()
    }

    @Provides
    @Singleton
    fun provideSavingsRepository(
        goalDao: GoalDao,
        contributionDao: ContributionDao,
        goalMapper: com.josphat.myakibabenki.data.mapper.GoalEntityMapper,
        contributionMapper: com.josphat.myakibabenki.data.mapper.ContributionEntityMapper
    ): SavingsRepository {
        return SavingsRepositoryImpl(
            goalDao = goalDao,
            contributionDao = contributionDao,
            goalMapper = goalMapper,
            contributionMapper = contributionMapper
        )
    }
}