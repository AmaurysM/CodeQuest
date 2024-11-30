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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
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
    var childIDs by mutableStateOf(user.children)
    var children = mutableStateListOf<User>()

    var newChild by mutableStateOf(User())
    var isAddingChild = mutableStateOf(false)
    var isEditingChild = mutableStateOf(false)
    var editingChild by mutableStateOf(User())

    init {

        updateChildren()

    }

    fun updateChildren() {
        user = FireBaseController.currentUser
        isParent = user.isAParent
        childIDs = user.children
        CoroutineScope(Dispatchers.Main).launch {
            childIDs.forEach { child ->
                launch {
                    children.add(FireBaseController.getUser(child))
                }.join()
                Log.e("TAG", "Children: ${children}")
                Log.e("TAG", "Children IDs: ${childIDs}")
            }
        }

    }

    fun areYouAParent(): Boolean {

        user = FireBaseController.currentUser
        isParent = user.isAParent
        childIDs = user.children

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

    suspend fun getChild(child: String): User {
        return withContext(Dispatchers.IO) {
            try {
                FireBaseController.getUser(child)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun addChild() {
        val database = Firebase.firestore

        var newChildEmail: User? = User()
        Log.e("TAG", "Child IDs: ${childIDs}")
        Log.e("TAG", "Children: ${children}")

        Log.e("TAG", "New Child: ${newChild}")

        CoroutineScope(Dispatchers.IO).launch {
            newChildEmail = FireBaseController.getUserFromEmail(newChild.email)

        }.join()
        database.collection("user")
            .document(user.userId)
            .update("children", user.children.plus(newChildEmail?.userId))

        isAddingChild.value = false
        updateChildren()


    }

    fun removeChild() {
        val database = Firebase.firestore
        database.collection("user")
            .document(user.userId)
            .update("children", user.children.minus(editingChild.userId))

    }


}