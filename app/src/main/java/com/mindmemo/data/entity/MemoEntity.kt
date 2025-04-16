package com.mindmemo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindmemo.data.utils.MEMO_TABLE

@Entity(tableName = MEMO_TABLE)
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var category: String = "",
    var priority: String = "",
    val dateCreated: String = ""
)

