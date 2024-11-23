package com.amaurysdm.codequest.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.CodeQuestScreens

class WelcomeViewmodel: ViewModel()  {

    fun navigateLogin(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Login.route)
    }

    fun navigateRegister(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Register.route)
    }
}