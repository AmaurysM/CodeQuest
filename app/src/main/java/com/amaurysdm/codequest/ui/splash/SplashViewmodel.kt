package com.amaurysdm.codequest.ui.splash

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.FireBaseController
import com.amaurysdm.codequest.navigation.Screens

class SplashViewmodel : ViewModel() {

    suspend fun navigateIfLogged(navController: NavHostController) {
        FireBaseController.beginningDestination({
            navController.navigate(Screens.General.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        },{
            navController.navigate(Screens.UserCreation.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        })

    }
}