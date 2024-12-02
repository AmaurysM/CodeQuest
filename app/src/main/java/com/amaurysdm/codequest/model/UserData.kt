package com.amaurysdm.codequest.model

// Complete user data.
// Some of the data is useless. Ex listLevels, parent
// Not familiar enough with firebase to know what would happen if I change it now.
data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val listLevels: List<GameState> = emptyList(),
    val children: List<String> = emptyList(),
    val parent: String = "",
    var isAParent: Boolean = false
)