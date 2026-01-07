package com.josphat.myakibabenki.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.josphat.myakibabenki.data.local.dao.ContributionDao
import com.josphat.myakibabenki.data.local.dao.GoalDao
import com.josphat.myakibabenki.data.local.entity.ContributionEntity
import com.josphat.myakibabenki.data.local.entity.GoalEntity

@Database(
    entities = [GoalEntity::class, ContributionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SavingsDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao
    abstract fun contributionDao(): ContributionDao
}