package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.domain.repository.HomeRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AllNoteUseCaseTest {

    @MockK
    lateinit var homeRepository: HomeRepository

    private lateinit var allNoteUseCase: AllNoteUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        allNoteUseCase = AllNoteUseCase(homeRepository)
    }

    @Test
    fun `getAllNote should return expected notes`() = runBlocking {
        val fakeNotes = listOf(
            MemoEntity(
                id = 1,
                title = "Test",
                description = "Desc",
                category = "General",
                priority = "LOW",
                dateCreated = "2025-04-20"
            )
        )

        every { homeRepository.getAllNote() } returns flowOf(fakeNotes.toMutableList())

        val result = mutableListOf<MemoEntity>()
        allNoteUseCase.getAllNote().collect {
            result.addAll(it)
        }

        assertEquals(fakeNotes, result)
        verify(exactly = 1) { homeRepository.getAllNote() }
    }
}