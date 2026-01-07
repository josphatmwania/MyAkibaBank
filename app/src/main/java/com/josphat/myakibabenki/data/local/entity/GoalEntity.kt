package com.josphat.myakibabenki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "goals",
    indices = [Index(value = ["id"])]
)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val targetDate: Long?,
    val createdAt: Long
)