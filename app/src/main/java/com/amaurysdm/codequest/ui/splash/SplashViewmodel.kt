package com.amaurysdm.codequest.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.controllers.room.RoomController
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.launch

class SplashViewmodel : ViewModel() {

    // checks if the user is logged in or not.
    // Navigates accordingly.
    fun navigateIfLogged(navController: NavHostController) {

        viewModelScope.launch {
            val user = RoomController.findLoggedInUser()
            if (user != null) {
                navController.navigate(Screens.General.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    RoomController.currentUser = user
                }
            } else {
                navController.navigate(Screens.UserCreation.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }

                }

            }
        }
        /*FireBaseController.beginningDestination({
            navController.navigate(Screens.General.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }, {
            navController.navigate(Screens.UserCreation.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        })*/

    }
}