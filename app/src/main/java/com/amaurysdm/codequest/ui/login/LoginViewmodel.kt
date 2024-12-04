package com.amaurysdm.codequest.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.controllers.room.RoomController
import com.amaurysdm.codequest.model.LoginData
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.launch

class LoginViewmodel : ViewModel() {
    var passwordVisible by mutableStateOf(false)
    var loginData by mutableStateOf(LoginData())

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun iconPassword(): Int {
        return if (passwordVisible) {
            R.drawable.baseline_visibility_24
        } else {
            R.drawable.baseline_visibility_off_24
        }
    }


    fun goBack(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Welcome.route) { // Navigate to the welcome screen
            popUpTo(Screens.UserCreationChild.Login.route) { // Remove the login screen from the back stack
                inclusive = true
            }
        }
        RoomController.cancelLogin() // Cancel the login job
    }

    fun login(navController: NavHostController) {
        if (loginData.email.isEmpty() || loginData.password.isEmpty()) {
            return
        }
        // Login to firebase
        viewModelScope.launch {
            RoomController.login(loginData.email, loginData.password)

        }.invokeOnCompletion {
            navController.navigate(Screens.General.route) {
                popUpTo(Screens.UserCreationChild.Login.route) {
                    inclusive = true
                }
            }
        }

    }

    fun goToRegister(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Register.route) {
            popUpTo(Screens.UserCreationChild.Login.route) {
                inclusive = true
            }
        }
    }
}