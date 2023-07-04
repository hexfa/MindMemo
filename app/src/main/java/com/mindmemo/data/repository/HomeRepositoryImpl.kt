package com.mindmemo.data.repository

import com.mindmemo.domain.repository.HomeRepository
import com.mindmemo.data.db.MemoDao
import com.mindmemo.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val noteDao: MemoDao) : HomeRepository {
    override fun getAllNote(): Flow<MutableList<MemoEntity>> {
        return noteDao.getAllNotes()
    }

    override fun searchNotes(search: String): Flow<MutableList<MemoEntity>> {
        return noteDao.searchNote(search)
    }

    override suspend fun deleteNote(entity: MemoEntity) {
        noteDao.deleteNote(entity)
    }
}