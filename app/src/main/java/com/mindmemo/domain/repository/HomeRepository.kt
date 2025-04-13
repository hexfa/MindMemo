package com.mindmemo.domain.repository

import com.mindmemo.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getAllNote(): Flow<MutableList<MemoEntity>>
    fun searchNotes(search: String): Flow<MutableList<MemoEntity>>
    suspend fun deleteNote(noteId: Int)
}