package com.amaurysdm.codequest.controllers.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amaurysdm.codequest.model.Level

@Dao
interface LevelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(level: Level)

    @Query("SELECT * FROM level_table WHERE route = :route")
    suspend fun getLevelByRoute(route: String): Level?

    @Query("SELECT * FROM level_table")
    suspend fun getAllLevels(): List<Level>

    @Query("SELECT * FROM level_table WHERE userId = :userId")
    suspend fun getLevelsCompletedByUser(userId: Int): List<Level>

}