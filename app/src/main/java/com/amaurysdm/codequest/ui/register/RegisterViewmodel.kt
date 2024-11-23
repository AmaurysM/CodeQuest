package com.amaurysdm.codequest.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.RegisterData
import com.amaurysdm.codequest.navigation.CodeQuestScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class RegisterViewmodel: ViewModel() {
    var registerData by mutableStateOf(RegisterData())
    //val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val auth: FirebaseAuth  = FirebaseAuth.getInstance()

    suspend fun register(navController: NavHostController){
        if(registerData.password != registerData.confirmPassword){
            return
        }
        auth.createUserWithEmailAndPassword(registerData.email, registerData.password).await()
        if(auth.currentUser != null){
            navController.navigate(CodeQuestScreens.Login.route)
        }
    }

    fun goBack(navController: NavHostController){
        navController.popBackStack()
    }

    fun goToLogin(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Login.route) {
            popUpTo(CodeQuestScreens.Register.route) {
                inclusive = true
            }
        }
    }
}