package com.amaurysdm.codequest.ui.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewmodel : ViewModel() {

    fun startGame(navController: NavHostController) {
        navController.navigate(Screens.GameChild.LevelSelect.route)
    }

    fun settings(navController: NavHostController) {
        navController.navigate(Screens.GeneralChild.Settings.route)
    }
}