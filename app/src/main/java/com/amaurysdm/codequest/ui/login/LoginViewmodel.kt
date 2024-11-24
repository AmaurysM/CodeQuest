package com.amaurysdm.codequest.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.LoginData
import com.amaurysdm.codequest.navigation.CodeQuestScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewmodel : ViewModel() {
    var loginData by mutableStateOf(LoginData())
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var correct by mutableStateOf(true)
    private var loginJob = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun goBack(navController: NavHostController) {
        navController.popBackStack()
        loginJob.cancel()
    }

    fun login(navController: NavHostController) {
        loginJob.launch {
            if (loginData.email.isEmpty() || loginData.password.isEmpty()) {
                return@launch
            }

            try {
                auth.signInWithEmailAndPassword(loginData.email, loginData.password).await()

                withContext(Dispatchers.Main) {
                    navController.navigate(CodeQuestScreens.Home.route)
                }
            } catch (e: Exception){
                println(e.message + " HELLLOOOOOOOOOOO!!!")
            }
        }
    }

    fun goToRegister(navController: NavHostController) {
        navController.navigate(CodeQuestScreens.Register.route) {
            popUpTo(CodeQuestScreens.Login.route) {
                inclusive = true
            }
        }
    }
    //private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
}