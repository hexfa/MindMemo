package com.mindmemo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.MEMO_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(entity: MemoEntity)

    @Update
    suspend fun updateNote(entity: MemoEntity)

    @Query("DELETE FROM $MEMO_TABLE WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM $MEMO_TABLE ORDER BY id DESC")
    fun getAllNotes(): Flow<MutableList<MemoEntity>>

    @Query("SELECT * FROM $MEMO_TABLE WHERE id = :id")
    fun getNote(id: Int): Flow<MemoEntity>

    @Query("SELECT * FROM $MEMO_TABLE WHERE title LIKE '%' || :query || '%' OR disc LIKE '%' || :query || '%'")
    fun searchNote(query: String): Flow<MutableList<MemoEntity>>

}