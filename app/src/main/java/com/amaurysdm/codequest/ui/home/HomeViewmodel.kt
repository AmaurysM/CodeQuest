package com.amaurysdm.codequest.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewmodel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var loginJob = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startGame(navController: NavHostController) {
        navController.navigate(Screens.GameChild.LevelSelect.route)
    }

    fun settings(navController: NavHostController) {
        navController.navigate(Screens.GeneralChild.Settings.route)
    }

    fun logout(navController: NavHostController) {
        loginJob.launch {
            try {
                auth.signOut()

                withContext(Dispatchers.Main) {
                    navController.navigate(Screens.UserCreationChild.Welcome.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            } catch (_: Exception) {

            }
        }
    }
}