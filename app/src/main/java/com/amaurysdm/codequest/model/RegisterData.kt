package com.amaurysdm.codequest.model

// Only used when registering. Used to track the input fields.
data class RegisterData(
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var areYouAParent: Boolean = false
)