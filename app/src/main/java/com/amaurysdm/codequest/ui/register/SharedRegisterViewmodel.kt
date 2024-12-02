package com.amaurysdm.codequest.ui.register

import androidx.lifecycle.ViewModel
import com.amaurysdm.codequest.model.User

/*Shared viewmodel between the parent or child view and the register view.
This is a singleton. I made this when I though I was going to make a lot of screens
to create the user. But at the end of the day I never came up with what should be
different between creating a child account and a parent account.

I my ideas got to big an something had to give.
*/
object SharedRegisterViewmodel : ViewModel() {
    val userBeingCreated = User()
}