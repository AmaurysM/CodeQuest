package com.amaurysdm.codequest.ui.splash

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.CodeQuestScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewmodel : ViewModel() {

    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    suspend fun navigateIfLogged(navController: NavHostController) {

        var isLoggedIn = false

        val checkLoginJob = GlobalScope.launch {
            isLoggedIn = auth.currentUser != null
        }

        delay(3000)

        checkLoginJob.join()

        if (isLoggedIn) {
            navController.navigate(CodeQuestScreens.General.route){
                popUpTo(navController.graph.startDestinationId){
                    inclusive = true
                }
            }
        } else {
            navController.navigate(CodeQuestScreens.UserCreation.route){
                popUpTo(navController.graph.startDestinationId){
                    inclusive = true
                }
            }
        }
    }
}