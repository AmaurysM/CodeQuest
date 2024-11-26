package com.amaurysdm.codequest.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens

class WelcomeViewmodel : ViewModel() {

    fun navigateLogin(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Login.route)
    }

    fun navigateRegister(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Register.route)
    }
}