package com.amaurysdm.codequest.ui.levelselect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.controllers.LevelController
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.navigation.Screens

class LevelSelectViewmodel : ViewModel() {

    var levels = LevelController.levels

    fun getLevels(): List<Level> {
        return LevelController.levels.value
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