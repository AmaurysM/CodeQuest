package com.amaurysdm.codequest.controllers.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amaurysdm.codequest.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM user_table WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM user_table WHERE loggedIn = 1")
    suspend fun findLoggedInUser(): User?

}