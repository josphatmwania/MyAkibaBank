package com.josphat.myakibabenki.domain.model

data class Goal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val targetDate: Long?,  // Timestamp in milliseconds
    val createdAt: Long
) {
    init {
        require(name.isNotBlank()) { "Goal name cannot be blank" }
        require(targetAmount > 0) { "Target amount must be greater than zero" }
    }
}