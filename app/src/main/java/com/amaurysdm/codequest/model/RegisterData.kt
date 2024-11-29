package com.amaurysdm.codequest.model

data class RegisterData(
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)