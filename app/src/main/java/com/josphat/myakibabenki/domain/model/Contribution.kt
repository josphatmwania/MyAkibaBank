package com.josphat.myakibabenki.domain.model

data class Contribution(
    val id: Long = 0,
    val goalId: Long,
    val amount: Double,
    val date: Long,  // Timestamp in milliseconds
    val createdAt: Long
) {
    init {
        require(amount > 0) { "Contribution amount must be greater than zero" }
        require(goalId > 0) { "Goal ID must be greater than zero" }
        require(date > 0) { "Date must be valid" }
    }
}