package com.mindmemo.domain.repository

import com.mindmemo.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun saveNote(noteEntity: MemoEntity)
    suspend fun updateNote(noteEntity: MemoEntity)
    fun detailNote(id : Int) : Flow<MemoEntity>

}