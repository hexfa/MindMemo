package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK
import com.mindmemo.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var updateUseCase: UpdateUseCase

    @Before
    fun setUp() {
        noteRepository = mockk()
        updateUseCase = UpdateUseCase(noteRepository)
    }

    @Test
    fun `updateNote should call repository updateNote with correct parameter`() = runTest {
        // Arrange
        val noteEntity = MemoEntity(
            id = 1,
            title = "Updated Note",
            description = "This is the updated note.",
            priority = NORMAL,
            category = WORK,
            dateCreated = "2025-01-02"
        )
        coEvery { noteRepository.updateNote(noteEntity) } returns Unit

        // Act
        updateUseCase.updateNote(noteEntity)

        // Assert
        coVerify(exactly = 1) { noteRepository.updateNote(noteEntity) }
    }
}