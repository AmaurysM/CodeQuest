package com.amaurysdm.codequest.ui.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.controllers.FireBaseController
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.User
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SettingsViewmodel : ViewModel() {


    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var user by mutableStateOf(FireBaseController.currentUser)
    var isParent by mutableStateOf(user.isAParent)
    var editingChild by mutableStateOf(User())
    var newChild by mutableStateOf(User())

    // First time using Flow.
    private val _levelsFlow = MutableStateFlow<List<Level>>(emptyList())
    val levelsFlow: StateFlow<List<Level>> get() = _levelsFlow

    private val _childrenFlow = MutableStateFlow<List<User>>(emptyList())
    val childrenFlow: StateFlow<List<User>> get() = _childrenFlow

    private val _editingChildLevelsFlow = MutableStateFlow<List<Level>>(emptyList())
    val editingChildLevelsFlow: StateFlow<List<Level>> get() = _editingChildLevelsFlow

    var isAddingChild by mutableStateOf(false)
    var isEditingChild by mutableStateOf(false)
    var dropDownActive by mutableStateOf(false)

    // initializes the data in the childrenFlow and levelsFlow
    init {
        updateChildrenFlow()
        updateLevelsFlow()
    }

    // just gets the levels that the user has completed from firebase
    private fun updateLevelsFlow() {
        viewModelScope.launch {
            _levelsFlow.value =
                FireBaseController.getCompletedLevelsByUser(user.userId).filterNotNull()
        }
    }

    // gets the children of the user from firebase
    private fun updateChildrenFlow() {
        viewModelScope.launch {
            _childrenFlow.value = user.children.mapNotNull { childId ->
                try {
                    FireBaseController.getUser(childId)
                } catch (e: Exception) {
                    null
                }
            }

        }.invokeOnCompletion {
            Log.e("TAG", "updateChildrenFlow: ${childrenFlow.value}")
        }
    }

    // gets the levels that the child has completed from firebase
    fun observeChildLevels(child: User) {
        viewModelScope.launch {
            _editingChildLevelsFlow.value =
                FireBaseController.getCompletedLevelsByUser(child.userId).filterNotNull()
            editingChild = child
        }
    }

    // logs out the user from firebase
    fun logout(navController: NavHostController) {
        viewModelScope.launch {
            try {
                auth.signOut()
                navController.navigate(Screens.UserCreationChild.Welcome.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            } catch (_: Exception) {

            }
        }
    }

    // removes the child from the user's children list
    fun removeChild() {
        viewModelScope.launch {
            try {
                val database = Firebase.firestore
                database.collection("user")
                    .document(user.userId)
                    .update("children", user.children.minus(editingChild.userId))
                    .await()

            } catch (_: Exception) {

            }
        }
    }

    // adds the child to the user's children list
    fun addChild() {

        viewModelScope.launch {
            try {
                val childFound = FireBaseController.getUserFromEmail(newChild.email)

                val database = Firebase.firestore
                if (childFound != null) {
                    database.collection("user")
                        .document(user.userId)
                        .update("children", user.children.plus(childFound.userId))
                        .await()

                }
            } catch (_: Exception) {

            }
        }
    }

    // navigates back to the general screen
    fun back(navController: NavHostController) {
        navController.navigate(Screens.General.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}