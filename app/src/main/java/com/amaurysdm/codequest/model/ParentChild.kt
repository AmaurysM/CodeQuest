package com.amaurysdm.codequest.model

import androidx.room.Entity

@Entity(tableName = "parent_child_table", primaryKeys = ["parentId", "childId"])
data class ParentChild(
    val parentId: Int,
    val childId: Int

)