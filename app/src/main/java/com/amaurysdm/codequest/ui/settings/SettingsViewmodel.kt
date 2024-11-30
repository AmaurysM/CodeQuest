package com.amaurysdm.codequest.ui.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.FireBaseController
import com.amaurysdm.codequest.model.User
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewmodel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var loginJob = CoroutineScope(Dispatchers.IO + SupervisorJob())
    var user by mutableStateOf(User())

    var isParent by mutableStateOf(user.isAParent)
    var children by mutableStateOf(user.children)

    init {
        user = FireBaseController.currentUser
        /*isParent = user.isAParent
        children = user.children*/

    }/**/

    suspend fun getChildren(): MutableList<User> {
        val foundChildren = mutableListOf<User>()
        children.forEach { child ->
            foundChildren.add(FireBaseController.getUser(child))
        }
        return foundChildren
    }

    fun areYouAParent(): Boolean {

            user = FireBaseController.currentUser
            isParent = user.isAParent
            children = user.children

        Log.e("isAParent", isParent.toString())
        return isParent
    }

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

    fun back(navController: NavHostController) {
        navController.navigate(Screens.General.route){
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }

    }


}