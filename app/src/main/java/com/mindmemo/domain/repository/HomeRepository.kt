package com.example.noteappcleanarchitecture.domain.repository

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getAllNote() : Flow<MutableList<NoteEntity>>
    fun searchNotes(search: String) : Flow<MutableList<NoteEntity>>
    suspend fun deleteNote(entity: NoteEntity)

}