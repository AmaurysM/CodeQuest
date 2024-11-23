package com.amaurysdm.codequest.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.AuthManager
import com.amaurysdm.codequest.model.LoginData
import com.amaurysdm.codequest.navigation.CodeQuestScreens
import com.google.firebase.auth.FirebaseAuth

class LoginViewmodel : ViewModel() {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var correct by mutableStateOf(true)


    fun goBack(navController: NavHostController) {
        AuthManager.logout()
        navController.popBackStack()
    }

    fun goToRegister(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Register.route) {
            popUpTo(CodeQuestScreens.Login.route) {
                inclusive = true
            }
        }
    }

    suspend fun login(navController: NavHostController) {
        AuthManager.login(loginData)
        navController.navigate(CodeQuestScreens.General.route)
    }

    //private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var loginData by mutableStateOf(LoginData())
}