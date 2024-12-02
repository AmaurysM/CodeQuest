package com.amaurysdm.codequest.ui.register.parentorchild

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens
import com.amaurysdm.codequest.ui.register.SharedRegisterViewmodel

// Navigates and updates the data of the user being created
class ParentOrChildViewmodel : ViewModel() {
    fun goToChild(navController: NavHostController) {
        SharedRegisterViewmodel.userBeingCreated.isAParent = false
        navController.navigate(Screens.UserCreationChild.Register.route) {
            popUpTo(Screens.UserCreationChild.ParentOrChild.route) {
                inclusive = true
            }
        }
    }

    fun goToParent(navController: NavHostController) {
        SharedRegisterViewmodel.userBeingCreated.isAParent = true
        navController.navigate(Screens.UserCreationChild.Register.route) {
            popUpTo(Screens.UserCreationChild.ParentOrChild.route) {
                inclusive = true
            }
        }

    }
}