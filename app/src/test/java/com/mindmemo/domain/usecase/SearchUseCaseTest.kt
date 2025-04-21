package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.EDUCATION
import com.mindmemo.data.utils.LOW
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK
import com.mindmemo.domain.repository.HomeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchUseCaseTest {

    private lateinit var homeRepository: HomeRepository
    private lateinit var searchUseCase: SearchUseCase

    @Before
    fun setUp() {
        homeRepository = mockk()
        searchUseCase = SearchUseCase(homeRepository)
    }

    @Test
    fun `searchNote should return filtered notes matching search query`() = runTest {
        // Arrange
        val searchQuery = "Test"
        val expectedNotes: MutableList<MemoEntity> = mutableListOf(
            MemoEntity(
                id = 1,
                title = "Test Note",
                description = "This is a test note.",
                priority = LOW,
                category = WORK,
                dateCreated = "2025-02-02"
            ),
            MemoEntity(
                id = 2,
                title = "Another Test",
                description = "Test description",
                priority = NORMAL,
                category = EDUCATION,
                dateCreated = "2025-02-03"
            )
        )
        every { homeRepository.searchNotes(searchQuery) } returns flowOf(expectedNotes)

        // Act
        val result = searchUseCase.searchNote(searchQuery).first()

        // Assert
        assertEquals(expectedNotes, result)
        verify(exactly = 1) { homeRepository.searchNotes(searchQuery) }
    }
}
