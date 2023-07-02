package com.mindmemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindmemo.data.entity.MemoEntity

@Database(entities = [MemoEntity::class], version = 1, exportSchema = false)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}