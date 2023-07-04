package com.example.noteappcleanarchitecture.data.repository

import com.example.noteappcleanarchitecture.data.db.NoteDao
import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : HomeRepository {
    override fun getAllNote(): Flow<MutableList<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    override fun searchNotes(search: String): Flow<MutableList<NoteEntity>> {
        return noteDao.searchNote(search)
    }

    override suspend fun deleteNote(entity: NoteEntity) {
        noteDao.deleteNote(entity)
    }
}