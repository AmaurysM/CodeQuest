package com.amaurysdm.codequest.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.FireBaseController
import com.amaurysdm.codequest.model.LoginData
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewmodel : ViewModel() {
    var loginData by mutableStateOf(LoginData())

    fun saveUser(){

        FireBaseController.saveLevels()
    }

    fun goBack(navController: NavHostController) {
        navController.popBackStack()
        FireBaseController.loginJob.cancel()
    }

    fun login(navController: NavHostController) {
        if (loginData.email.isEmpty() || loginData.password.isEmpty()) {
            return
        }
        FireBaseController.login(loginData, {
            navController.navigate(Screens.General.route) {
                popUpTo(Screens.UserCreationChild.Login.route) {
                    inclusive = true
                }
            }
        })
    }

    fun goToRegister(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Register.route) {
            popUpTo(Screens.UserCreationChild.Login.route) {
                inclusive = true
            }
        }
    }
}