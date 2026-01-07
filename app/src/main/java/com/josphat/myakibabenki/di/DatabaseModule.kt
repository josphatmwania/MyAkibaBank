package com.josphat.myakibabenki.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

private const val TAG = "DatabaseModule"

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseCoroutineScope

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
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d(TAG, "Database created: savings_database")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d(TAG, "Database opened: savings_database")
                }
            })
            .build()
    }

    @Provides
    @DatabaseCoroutineScope
    fun provideDatabaseCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
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
        contributionMapper: com.josphat.myakibabenki.data.mapper.ContributionEntityMapper,
        @DatabaseCoroutineScope databaseScope: CoroutineScope
    ): SavingsRepository {
        val repository = SavingsRepositoryImpl(
            goalDao = goalDao,
            contributionDao = contributionDao,
            goalMapper = goalMapper,
            contributionMapper = contributionMapper
        )

        // Log database state on initialization
        databaseScope.launch {
            try {
                val goalsCount = repository.getGoalsCount()
                val contributionsCount = repository.getContributionsCount()
                Log.d(TAG, "Database initialized - Goals: $goalsCount, Contributions: $contributionsCount")
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing database state check", e)
            }
        }

        return repository
    }
}