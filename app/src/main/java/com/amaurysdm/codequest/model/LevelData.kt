package com.amaurysdm.codequest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Gets pushed to firebase.
// Keeps track of the name, route and if the level has been completed by the user.
@Entity(tableName = "level_table")
data class Level(
    val name: String,
    val route: String,
    var completed: Boolean = false,
    var userId: Int = 0,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
)