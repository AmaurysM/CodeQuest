package com.amaurysdm.codequest.ui.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.FireBaseController
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
    private var database = Firebase.firestore

    var user by mutableStateOf(FireBaseController.currentUser)
    var isParent by mutableStateOf(user.isAParent)
    var editingChild by mutableStateOf(User())
    var newChild by mutableStateOf(User())

    private val _childrenFlow = MutableStateFlow<List<User>>(emptyList())
    val childrenFlow: StateFlow<List<User>> get() = _childrenFlow

    private val _editingChildLevelsFlow = MutableStateFlow<List<Level>>(emptyList())
    val editingChildLevelsFlow: StateFlow<List<Level>> get() = _editingChildLevelsFlow

    var isAddingChild by mutableStateOf(false)
    var isEditingChild by mutableStateOf(false)
    var dropDownActive by mutableStateOf(false)

    init {
        updateChildrenFlow()
    }

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

    fun updateChildrenList(newChildren: List<User>) {
        _childrenFlow.value = newChildren
    }

    fun observeChildLevels(child: User) {
        viewModelScope.launch {
            _editingChildLevelsFlow.value =
                FireBaseController.getCompletedLevelsByUser(child.userId).filterNotNull()
            editingChild = child
        }
    }

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
        Log.e("TAG", "addChild: ${childrenFlow}")
    }

    fun back(navController: NavHostController) {
        navController.navigate(Screens.General.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }

    }


    /*suspend fun startEditingChild(it: User) {
        isEditingChild = true
        editingChild = it
        getChildLevels()
    }*/

    /*

        var editingChildLevels = mutableStateListOf<Level>()
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
        var dropDownActive by mutableStateOf(false)

        init {
            user = FireBaseController.currentUser
            isParent = user.isAParent
            childIDs = user.children
            // updateChildren()

        }

        fun updateChildren() {
            children.clear()
            CoroutineScope(Dispatchers.Main).launch {
                childIDs.forEach { child ->
                        children.add(FireBaseController.getUser(child))
                }
            }

        }

        fun areYouAParent(): Boolean {
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
            navController.navigate(Screens.General.route) {
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
            CoroutineScope(Dispatchers.IO).launch {
                newChildEmail = FireBaseController.getUserFromEmail(newChild.email)

            }.join()
            database.collection("user")
                .document(user.userId)
                .update("children", user.children.plus(newChildEmail?.userId))

            isAddingChild.value = false
            dropDownActive = false


        }

        fun removeChild() {

            val database = Firebase.firestore
            database.collection("user")
                .document(user.userId)
                .update("children", user.children.minus(editingChild.userId))
            isEditingChild.value = false
            updateChildren()
        }


        suspend fun getChildLevels(){
            editingChildLevels.clear()

            val database = Firebase.firestore

            CoroutineScope(Dispatchers.IO).launch {
                FireBaseController.getCompletedLevelsByUser(editingChild.userId).forEach {
                    if (it != null) editingChildLevels.add(it)
                }

            }

        }

        suspend fun startEditingChild(it: User) {
            isEditingChild.value = true
            editingChild = it
            getChildLevels()
        }

        fun dropDown() {
            dropDownActive = !dropDownActive
            updateChildren()

        }

    */

}