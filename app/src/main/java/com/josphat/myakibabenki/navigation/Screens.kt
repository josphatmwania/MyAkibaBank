package com.josphat.myakibabenki.navigation

/**
 * Central registry of all screens in the application.
 * This provides a single source of truth for all navigation destinations.
 */
object Screens {
    // Main screens
    val Home = Screen.Home
    val CreateGoal = Screen.CreateGoal
    val GoalDetail = Screen.GoalDetail

    /**
     * List of all top-level screens (screens that can be reached from anywhere)
     */
    val topLevelScreens = listOf(
        Home
    )

    /**
     * List of all screens in the app
     */
    val allScreens = listOf(
        Home,
        CreateGoal,
        GoalDetail
    )
}

