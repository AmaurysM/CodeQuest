package com.amaurysdm.codequest.ui.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.FireBaseController
import com.amaurysdm.codequest.model.RegisterData
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.cancel

class RegisterViewmodel : ViewModel() {
    var registerData by mutableStateOf(RegisterData())

    fun register(navController: NavHostController) {
        if (registerData.email.isEmpty() || registerData.password.isEmpty() || registerData.confirmPassword.isEmpty()) {
            return
        }

        if (registerData.password != registerData.confirmPassword) {
            return
        }

        registerData.areYouAParent = SharedRegisterViewmodel.userBeingCreated.isAParent

        Log.e("registerData", registerData.toString())

        FireBaseController.register(registerData, onRegister = {
            navController.navigate(Screens.UserCreationChild.Login.route)
        })
    }

    fun goBack(navController: NavHostController) {
        navController.popBackStack()
        FireBaseController.registerJob.cancel()
    }

    fun goToLogin(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Login.route) {
            popUpTo(Screens.UserCreationChild.Register.route) {
                inclusive = true
            }
        }
    }
}