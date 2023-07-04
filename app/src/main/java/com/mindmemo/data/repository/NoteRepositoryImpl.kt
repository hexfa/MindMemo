package com.example.noteappcleanarchitecture.data.repository

import com.example.noteappcleanarchitecture.data.db.NoteDao
import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun saveNote(noteEntity: NoteEntity) {
        noteDao.saveNote(noteEntity)
    }

    override suspend fun updateNote(noteEntity: NoteEntity) {
        noteDao.updateNote(noteEntity)
    }

    override fun detailNote(id: Int): Flow<NoteEntity> {
        return noteDao.getNote(id)
    }
}