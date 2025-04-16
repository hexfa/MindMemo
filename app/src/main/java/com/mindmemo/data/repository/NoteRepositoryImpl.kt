package com.mindmemo.data.repository

import com.mindmemo.data.db.MemoDao
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(private val noteDao: MemoDao) : NoteRepository {
    override suspend fun saveNote(noteEntity: MemoEntity): Long {
        return noteDao.saveNote(noteEntity)
    }

    override suspend fun updateNote(noteEntity: MemoEntity) {
        noteDao.updateNote(noteEntity)
    }

    override fun detailNote(id: Int): Flow<MemoEntity> {
        return noteDao.getNote(id)
    }
}