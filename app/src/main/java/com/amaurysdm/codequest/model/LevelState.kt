package com.amaurysdm.codequest.model

// Gets pushed to firebase.
// Keeps track of the name, route and if the level has been completed by the user.
data class Level(val name: String, val route: String, var completed: Boolean = false)