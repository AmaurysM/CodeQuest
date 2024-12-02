package com.amaurysdm.codequest.navigation

// Trying something new.
// Don't know if I should separate the screens or not.
sealed class Screens(val route: String) {
    // Splash
    data object Splash : Screens("splash")

    // Parent Navigation
    data object UserCreation : Screens("userCreation")
    data object General : Screens("general")

    // Child Navigation: UserCreation
    sealed class UserCreationChild(route: String) : Screens(route) {
        data object Welcome : UserCreationChild("welcome")
        data object Login : UserCreationChild("login")
        data object Register : UserCreationChild("register")
        data object ParentOrChild : UserCreationChild("parentOrChild")
    }

    // Child Navigation: General
    sealed class GeneralChild(route: String) : Screens(route) {
        data object Home : GeneralChild("home")
        data object Profile : GeneralChild("profile")
        data object Settings : GeneralChild("settings")
    }

    // Child Navigation: Game
    sealed class GameChild(route: String) : Screens(route) {
        data object Game : GameChild("game")
        data object LevelSelect : GameChild("levelSelect")
        data object Level : GameChild("level")
    }
}
