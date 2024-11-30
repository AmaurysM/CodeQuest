package com.amaurysdm.codequest.ui.register

import androidx.lifecycle.ViewModel
import com.amaurysdm.codequest.model.User

object SharedRegisterViewmodel: ViewModel() {
    val userBeingCreated = User()
}