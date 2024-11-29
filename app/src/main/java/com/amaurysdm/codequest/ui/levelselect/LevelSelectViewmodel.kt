package com.amaurysdm.codequest.ui.levelselect

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.FireBaseController
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.LevelController
import com.amaurysdm.codequest.navigation.Screens

class LevelSelectViewmodel : ViewModel() {
    val levels = LevelController.levels.value.size
    //val levelsCompletedByUser = FireBaseController.getCompletedLevels()

    fun getLevel(it: Int): Level {
        return LevelController.getCertainLevel(it)
    }

    fun navigateToLevel(navController: NavHostController, it: Int) {
        LevelController.setLevel(it)
        navController.navigate(Screens.GameChild.Level.route)
    }

    fun leaveLevelSelect(navController: NavHostController) {
        navController.popBackStack()
    }
}