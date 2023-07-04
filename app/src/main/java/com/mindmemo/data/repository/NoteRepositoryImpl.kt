package com.mindmemo.data.repository

import com.mindmemo.domain.repository.NoteRepository
import com.mindmemo.data.db.MemoDao
import com.mindmemo.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: MemoDao) : NoteRepository {
    override suspend fun saveNote(noteEntity: MemoEntity) {
        noteDao.saveNote(noteEntity)
    }

    override suspend fun updateNote(noteEntity: MemoEntity) {
        noteDao.updateNote(noteEntity)
    }

    override fun detailNote(id: Int): Flow<MemoEntity> {
        return noteDao.getNote(id)
    }
}