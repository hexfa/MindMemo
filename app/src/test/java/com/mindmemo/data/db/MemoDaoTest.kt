package com.mindmemo.data.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindmemo.data.entity.MemoEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MemoDaoTest {

    private lateinit var database: MemoDatabase
    private lateinit var memoDao: MemoDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MemoDatabase::class.java
        ).allowMainThreadQueries().build()
        memoDao = database.memoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertNoteAndGetAllNotes() = runTest {
        val note = MemoEntity(1, "Title", "Desc", "HOME", "NORMAL", "2025-04-21")
        memoDao.saveNote(note)

        val result = memoDao.getAllNotes().first()
        assertEquals(1, result.size)
        assertEquals("Title", result.first().title)
    }

    @Test
    fun updateNoteTest() = runTest {
        val note = MemoEntity(1, "Title", "Desc", "HOME", "NORMAL", "2025-04-21")
        memoDao.saveNote(note)

        val updatedNote = note.copy(title = "Updated")
        memoDao.updateNote(updatedNote)

        val result = memoDao.getNote(1).first()
        assertEquals("Updated", result.title)
    }

    @Test
    fun deleteNoteTest() = runTest {
        val note = MemoEntity(1, "Title", "Desc", "HOME", "NORMAL", "2025-04-21")
        memoDao.saveNote(note)

        memoDao.deleteNote(note.id)
        val result = memoDao.getAllNotes().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun searchNoteTest() = runTest {
        val note1 = MemoEntity(1, "Meeting", "Discuss project", "WORK", "HIGH", "2025-04-21")
        val note2 = MemoEntity(2, "Shopping", "Buy groceries", "HOME", "LOW", "2025-04-21")
        memoDao.saveNote(note1)
        memoDao.saveNote(note2)

        val result = memoDao.searchNote("project").first()
        assertEquals(1, result.size)
        assertEquals("Meeting", result.first().title)
    }
}