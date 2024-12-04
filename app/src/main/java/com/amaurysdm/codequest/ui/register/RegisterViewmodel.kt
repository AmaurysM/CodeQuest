package com.amaurysdm.codequest.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.controllers.room.RoomController
import com.amaurysdm.codequest.model.RegisterData
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewmodel : ViewModel() {
    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)
    var registerData by mutableStateOf(RegisterData())


    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        confirmPasswordVisible = !confirmPasswordVisible
    }

    fun iconPassword(): Int {
        return if (passwordVisible) {
            R.drawable.baseline_visibility_off_24
        } else {
            R.drawable.baseline_visibility_24
        }

    }

    fun iconConfirmPassword(): Int {
        return if (confirmPasswordVisible) {
            R.drawable.baseline_visibility_off_24
        } else {
            R.drawable.baseline_visibility_24
        }
    }

    fun register(navController: NavHostController) {
        if (registerData.email.isEmpty() || registerData.password.isEmpty() || registerData.confirmPassword.isEmpty()) {
            return
        }

        if (registerData.password != registerData.confirmPassword) {
            return
        }

        registerData.areYouAParent = SharedRegisterViewmodel.userBeingCreated.isAParent


        viewModelScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT) {

            if (RoomController.getUserByMail(registerData.email) == null) {
                RoomController.register(
                    registerData.username,
                    registerData.email,
                    registerData.password,
                    registerData.areYouAParent
                )
            }

        }
        goToLogin(navController)


    }

    fun goBack(navController: NavHostController) {
        navController.popBackStack()
        RoomController.cancelRegister()
    }

    fun goToLogin(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Login.route) {
            popUpTo(Screens.UserCreationChild.Register.route) {
                inclusive = true
            }
        }
    }
}