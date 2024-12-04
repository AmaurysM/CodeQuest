package com.amaurysdm.codequest.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.controllers.room.RoomController
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.User
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewmodel : ViewModel() {

    var user by mutableStateOf(RoomController.currentUser)
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

    // just gets the levels that the user has completed from Room
    private fun updateLevelsFlow() {
        viewModelScope.launch {
            _levelsFlow.value =
                RoomController.getLevelsCompletedByUser(user.userId ?: 0).filterNotNull()
        }
    }

    // gets the children of the user from Room
    private fun updateChildrenFlow() {
        viewModelScope.launch {
            _childrenFlow.value = RoomController.getChildren(user.userId ?: 0)
        }

    }


    // gets the levels that the child has completed from Room
    fun observeChildLevels(child: User) {
        viewModelScope.launch {
            _editingChildLevelsFlow.value =
                RoomController.getLevelsCompletedByUser(child.userId ?: 0).filterNotNull()
            editingChild = child
        }
    }

    // logs out the user from Room
    fun logout(navController: NavHostController) {
        viewModelScope.launch {
            try {
                RoomController.logout()
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
            RoomController.removeChild(user.userId ?: 0, editingChild.userId ?: 0)
            updateChildrenFlow()
        }

    }

    // adds the child to the user's children list
    fun addChild() {

        viewModelScope.launch {


            val foundUser = RoomController.getUserByMail(newChild.email)
            if (foundUser != null) {
                RoomController.addChild(user.userId ?: 0, foundUser.userId ?: 0)

            }

            /*val childrenFound = RoomController.getChildren(user.userId ?: 0)//RoomController.getUserFromEmail(newChild.email)
            _childrenFlow.value = childrenFound*/
            updateChildrenFlow()


        }
    }

    // navigates back to the general screen
    fun back(navController: NavHostController) {
        RoomController.cancelLogout()
        navController.navigate(Screens.General.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

}