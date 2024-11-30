package com.amaurysdm.codequest.model

data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val listLevels: List<GameState> = emptyList(),
    val children: List<String> = emptyList(),
    val parent: String = "",
    var isAParent: Boolean = false
)