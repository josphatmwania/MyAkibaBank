package com.josphat.myakibabenki.domain.model

data class GoalWithProgress(
    val goal: Goal,
    val contributions: List<Contribution>,
    val totalSaved: Double = contributions.sumOf { it.amount },
    val remainingAmount: Double = kotlin.math.max(0.0, goal.targetAmount - totalSaved),
    val progressPercentage: Float = kotlin.math.min(
        100f,
        (totalSaved / goal.targetAmount * 100f).toFloat()
    ),
    val isCompleted: Boolean = totalSaved >= goal.targetAmount
)