package com.amaurysdm.codequest.ui.settings

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewmodel: ViewModel() {


    private val auth: FirebaseAuth =  FirebaseAuth.getInstance()
    val userEmail: String = auth.currentUser?.email ?: ""
    val userUid: String = auth.currentUser?.uid ?: ""
    private var loginJob = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun logout(navController: NavHostController) {
        loginJob.launch {
            try {
                auth.signOut()

                withContext(Dispatchers.Main) {
                    navController.navigate(Screens.UserCreationChild.Welcome.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            } catch (_: Exception) {

            }
        }
    }
}