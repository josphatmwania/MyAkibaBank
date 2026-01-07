package com.josphat.myakibabenki.domain.usecase

import com.josphat.myakibabenki.domain.model.Goal
import com.josphat.myakibabenki.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllGoalsUseCase @Inject constructor(
    private val repository: SavingsRepository
) {
    operator fun invoke(): Flow<List<Goal>> {
        return repository.observeAllGoals()
    }
}