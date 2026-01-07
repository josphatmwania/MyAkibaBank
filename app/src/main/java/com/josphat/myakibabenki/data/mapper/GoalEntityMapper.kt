package com.josphat.myakibabenki.data.mapper

import com.josphat.myakibabenki.data.local.entity.GoalEntity
import com.josphat.myakibabenki.domain.model.Goal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalEntityMapper @Inject constructor() {

    fun toDomain(entity: GoalEntity): Goal {
        return Goal(
            id = entity.id,
            name = entity.name,
            targetAmount = entity.targetAmount,
            targetDate = entity.targetDate,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Goal): GoalEntity {
        return GoalEntity(
            id = domain.id,
            name = domain.name,
            targetAmount = domain.targetAmount,
            targetDate = domain.targetDate,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<GoalEntity>): List<Goal> {
        return entities.map { toDomain(it) }
    }
}