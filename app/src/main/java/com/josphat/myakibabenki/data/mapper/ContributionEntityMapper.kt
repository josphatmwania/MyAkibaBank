package com.josphat.myakibabenki.data.mapper

import com.josphat.myakibabenki.data.local.entity.ContributionEntity
import com.josphat.myakibabenki.domain.model.Contribution
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContributionEntityMapper @Inject constructor() {

    fun toDomain(entity: ContributionEntity): Contribution {
        return Contribution(
            id = entity.id,
            goalId = entity.goalId,
            amount = entity.amount,
            date = entity.date,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Contribution): ContributionEntity {
        return ContributionEntity(
            id = domain.id,
            goalId = domain.goalId,
            amount = domain.amount,
            date = domain.date,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<ContributionEntity>): List<Contribution> {
        return entities.map { toDomain(it) }
    }
}