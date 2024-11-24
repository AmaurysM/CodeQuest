package com.amaurysdm.codequest.ui.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.CodeQuestScreens
import com.google.firebase.auth.FirebaseAuth

class HomeViewmodel: ViewModel() {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun startGame(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Game.route)
    }

    fun settings(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Settings.route)
    }

    fun logout(navController: NavHostController) {
        auth.signOut()
        navController.navigate(CodeQuestScreens.Login.route)

    }
}