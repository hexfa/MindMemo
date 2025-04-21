package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.LOW
import com.mindmemo.domain.repository.NoteRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import kotlinx.coroutines.flow.first

class DetailUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var detailUseCase: DetailUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        detailUseCase = DetailUseCase(noteRepository)
    }

    @Test
    fun `detailNote returns expected MemoEntity`() = runTest {
        // Arrange
        val noteId = 1
        val expectedNote = MemoEntity(
            id = noteId,
            title = "Test Note",
            description = "This is a test note.",
            priority = LOW,
            category = "Test",
            dateCreated = "2025-02-03"
        )
        every { noteRepository.detailNote(noteId) } returns flowOf(expectedNote)

        // Act
        val result = detailUseCase.detailNote(noteId).first()

        // Assert
        assertEquals(expectedNote, result)
        verify(exactly = 1) { noteRepository.detailNote(noteId) }
    }
}