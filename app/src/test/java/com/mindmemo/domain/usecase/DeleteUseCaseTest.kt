package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteUseCaseTest {

    private lateinit var homeRepository: HomeRepository
    private lateinit var deleteUseCase: DeleteUseCase

    @Before
    fun setUp() {
        homeRepository = mockk()
        deleteUseCase = DeleteUseCase(homeRepository)
    }

    @Test
    fun `deleteNote calls repository with correct noteId`() = runTest {
        // Arrange
        val noteId = 42
        coEvery { homeRepository.deleteNote(noteId) } returns Unit

        // Act
        deleteUseCase.deleteNote(noteId)

        // Assert
        coVerify(exactly = 1) { homeRepository.deleteNote(noteId) }
    }
}