package com.mindmemo.data.repository

import app.cash.turbine.test
import com.mindmemo.data.db.MemoDao
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NoteRepositoryImplTest {

    private lateinit var repository: NoteRepositoryImpl
    private val memoDao: MemoDao = mockk()

    @Before
    fun setUp() {
        repository = NoteRepositoryImpl(memoDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `saveNote should return note id`() = runTest {
        // Given
        val note = MemoEntity(
            id = 5,
            title = "Detail",
            description = "Note Detail",
            category = WORK,
            dateCreated = "2024-02-02",
            priority = NORMAL
        )
        val expectedId = 10L
        coEvery { memoDao.saveNote(note) } returns expectedId

        // When
        val result = repository.saveNote(note)

        // Then
        assertEquals(expectedId, result)
        coVerify(exactly = 1) { memoDao.saveNote(note) }
    }

    @Test
    fun `updateNote should call dao with correct entity`() = runTest {
        // Given
        val note = MemoEntity(
            id = 5,
            title = "Detail",
            description = "Note Detail",
            category = WORK,
            dateCreated = "2024-02-02",
            priority = NORMAL
        )
        coEvery { memoDao.updateNote(note) } just Runs

        // When
        repository.updateNote(note)

        // Then
        coVerify(exactly = 1) { memoDao.updateNote(note) }
    }

    @Test
    fun `detailNote should emit the note by id`() = runTest {
        // Given
        val noteId = 5
        val note = MemoEntity(
            id = 5,
            title = "Detail",
            description = "Note Detail",
            category = WORK,
            dateCreated = "2024-02-02",
            priority = NORMAL
        )
        every { memoDao.getNote(noteId) } returns flowOf(note)

        // When & Then
        repository.detailNote(noteId).test {
            val result = awaitItem()
            assertEquals(note, result)
            awaitComplete()
        }

        verify(exactly = 1) { memoDao.getNote(noteId) }
    }
}