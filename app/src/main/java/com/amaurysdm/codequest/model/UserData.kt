package com.amaurysdm.codequest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Complete user data.
// Some of the data is useless. Ex listLevels, parent
// Not familiar enough with firebase to know what would happen if I change it now.
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int? = null,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var isAParent: Boolean = false,
    var loggedIn: Boolean = false,
)
