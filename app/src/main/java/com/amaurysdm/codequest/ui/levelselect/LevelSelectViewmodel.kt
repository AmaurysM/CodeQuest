package com.amaurysdm.codequest.ui.levelselect

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.levels.LevelController
import com.amaurysdm.codequest.navigation.Screens

class LevelSelectViewmodel: ViewModel() {
    val levels = LevelController.numberOfLevels()

    fun navigateToLevel(navController: NavHostController, it: Int) {
        LevelController.setLevel(it)
        navController.navigate(Screens.GameChild.Level.route)
    }

    fun leaveLevelSelect(navController: NavHostController) {
        navController.popBackStack()
    }
}