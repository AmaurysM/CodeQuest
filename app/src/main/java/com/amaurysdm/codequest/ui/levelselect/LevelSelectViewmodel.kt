package com.amaurysdm.codequest.ui.levelselect

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.LevelController
import com.amaurysdm.codequest.navigation.Screens

class LevelSelectViewmodel : ViewModel() {

    var levels = LevelController.levels.value.size

    fun getLevelsCompletedByUser(): List<Level> {
        return LevelController.levels.value.filter { it.completed }
    }

    fun getLevel(it: Int): Level {
        return LevelController.getCertainLevel(it)
    }

    fun navigateToLevel(navController: NavHostController, it: Int) {
        LevelController.setLevel(it)
        navController.navigate(Screens.GameChild.Level.route)
    }

    fun leaveLevelSelect(navController: NavHostController) {
        navController.navigate(Screens.General.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}