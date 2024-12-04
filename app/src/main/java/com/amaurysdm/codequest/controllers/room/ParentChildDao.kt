package com.amaurysdm.codequest.controllers.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amaurysdm.codequest.model.ParentChild

@Dao
interface ParentChildDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParentChild(parentChild: ParentChild)

    @Query("SELECT * FROM parent_child_table WHERE childId = :child")
    suspend fun getParents(child: Int): List<ParentChild>

    @Query("SELECT * FROM parent_child_table WHERE parentId = :parent")
    suspend fun getChildren(parent: Int): List<ParentChild>

    @Query("DELETE FROM parent_child_table WHERE parentId = :parent AND childId = :child")
    suspend fun removeChild(parent: Int, child: Int)

}