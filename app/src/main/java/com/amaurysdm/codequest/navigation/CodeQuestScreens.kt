package com.amaurysdm.codequest.navigation

import androidx.compose.ui.graphics.Color
import com.amaurysdm.codequest.R

sealed class CodeQuestScreens(val route: String, val title: String? = null, val icon: Int = 0) {
    data object UserCreation: CodeQuestScreens("userCreation")
    data object General: CodeQuestScreens("general")

    data object Splash: CodeQuestScreens("splash")
    data object Welcome: CodeQuestScreens("welcome")
    data object Login: CodeQuestScreens("login")
    data object Register: CodeQuestScreens("register")

    data object Home: CodeQuestScreens("home")
    data object Profile: CodeQuestScreens("profile")
    data object Settings: CodeQuestScreens("settings")

    data object Game: CodeQuestScreens("game")
    data object LevelSelect: CodeQuestScreens("levelSelect")
    data object Level: CodeQuestScreens("level")
}
