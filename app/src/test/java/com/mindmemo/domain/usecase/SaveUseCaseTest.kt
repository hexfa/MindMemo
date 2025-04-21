package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var saveUseCase: SaveUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        saveUseCase = SaveUseCase(noteRepository)
    }

    @Test
    fun `saveNote should call repository saveNote with correct parameter and return id`() =
        runTest {
            // Arrange
            val noteEntity = MemoEntity(
                id = 1,
                title = "Test Note",
                description = "This is a test note.",
                priority = NORMAL,
                category = "General",
                dateCreated = "2025-02-03"
            )
            val expectedId = 42L
            coEvery { noteRepository.saveNote(noteEntity) } returns expectedId

            // Act
            val result = saveUseCase.saveNote(noteEntity)

            // Assert
            assertEquals(expectedId, result)
            coVerify(exactly = 1) { noteRepository.saveNote(noteEntity) }
        }
}