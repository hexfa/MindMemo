package com.example.noteappcleanarchitecture.domain.repository

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun saveNote(noteEntity: NoteEntity)
    suspend fun updateNote(noteEntity: NoteEntity)
    fun detailNote(id : Int) : Flow<NoteEntity>

}