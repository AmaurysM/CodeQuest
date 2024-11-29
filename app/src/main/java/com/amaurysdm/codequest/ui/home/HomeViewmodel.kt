package com.amaurysdm.codequest.ui.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens

class HomeViewmodel : ViewModel() {

    fun startGame(navController: NavHostController) {
        navController.navigate(Screens.GameChild.LevelSelect.route)
    }

    fun settings(navController: NavHostController) {
        navController.navigate(Screens.GeneralChild.Settings.route)
    }
}