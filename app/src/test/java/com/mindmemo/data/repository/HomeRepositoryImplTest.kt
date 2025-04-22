package com.mindmemo.data.repository

import app.cash.turbine.test
import com.mindmemo.data.db.MemoDao
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.EDUCATION
import com.mindmemo.data.utils.HEALTH
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.LOW
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

class HomeRepositoryImplTest {

    private lateinit var repository: HomeRepositoryImpl
    private val memoDao: MemoDao = mockk()

    @Before
    fun setUp() {
        repository = HomeRepositoryImpl(memoDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllNote should emit list of notes`() = runTest {
        // Given
        val notes = mutableListOf(
            MemoEntity(
                id = 1,
                title = "Title 1",
                description = "Desc 1",
                category = WORK,
                dateCreated = "",
                priority = HIGH
            ),
            MemoEntity(
                id = 2,
                title = "Title 2",
                description = "Desc 2",
                category = HEALTH,
                dateCreated = "",
                priority = LOW
            )
        )
        every { memoDao.getAllNotes() } returns flowOf(notes)

        // When & Then
        repository.getAllNote().test {
            val result = awaitItem()
            assertEquals(notes, result)
            awaitComplete()
        }

        verify(exactly = 1) { memoDao.getAllNotes() }
    }

    @Test
    fun `searchNotes should emit filtered list`() = runTest {
        // Given
        val query = "Title"
        val searchResult = mutableListOf(
            MemoEntity(
                id = 1,
                title = "Title 1",
                description = "Some text",
                category = EDUCATION,
                dateCreated = "2024-01-01",
                priority = NORMAL
            )
        )
        every { memoDao.searchNote(query) } returns flowOf(searchResult)

        // When & Then
        repository.searchNotes(query).test {
            val result = awaitItem()
            assertEquals(searchResult, result)
            awaitComplete()
        }

        verify(exactly = 1) { memoDao.searchNote(query) }
    }

    @Test
    fun `deleteNote should call dao with correct id`() = runTest {
        // Given
        val noteId = 42
        coEvery { memoDao.deleteNote(noteId) } just Runs

        // When
        repository.deleteNote(noteId)

        // Then
        coVerify(exactly = 1) { memoDao.deleteNote(noteId) }
    }
}