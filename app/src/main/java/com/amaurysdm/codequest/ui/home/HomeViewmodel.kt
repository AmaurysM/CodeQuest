package com.amaurysdm.codequest.ui.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.AuthManager
import com.amaurysdm.codequest.navigation.CodeQuestScreens

class HomeViewmodel: ViewModel() {
    fun startGame(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Game.route)
    }

    fun settings(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Settings.route)
    }
    
}