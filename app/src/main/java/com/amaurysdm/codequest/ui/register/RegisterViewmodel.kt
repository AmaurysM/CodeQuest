package com.amaurysdm.codequest.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.RegisterData
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

class RegisterViewmodel : ViewModel() {
    var registerData by mutableStateOf(RegisterData())

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun register(navController: NavHostController) {
        if (registerData.email.isEmpty() || registerData.password.isEmpty() || registerData.confirmPassword.isEmpty()) {
            return
        }

        if (registerData.password != registerData.confirmPassword) {
            return
        }

        auth.createUserWithEmailAndPassword(registerData.email, registerData.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(Screens.UserCreationChild.Login.route)
                }
            }
    }

    fun goBack(navController: NavHostController) {
        navController.popBackStack()
    }

    fun goToLogin(navController: NavHostController) {
        navController.navigate(Screens.UserCreationChild.Login.route) {
            popUpTo(Screens.UserCreationChild.Register.route) {
                inclusive = true
            }
        }
    }
}