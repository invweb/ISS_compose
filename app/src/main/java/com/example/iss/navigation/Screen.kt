package com.example.iss.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Splash : Screen()

    @Serializable
    object Menu : Screen()

    @Serializable
    object Map : Screen()

    @Serializable
    object Settings : Screen()

    @Serializable
    object Astro : Screen()

    @Serializable
    data class Detail(val name: String, val craft: String) : Screen()
}
