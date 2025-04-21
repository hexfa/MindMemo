package com.mindmemo.presentation.viewmodel

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DataStatus
import com.mindmemo.data.utils.HEALTH
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.LOW
import com.mindmemo.data.utils.WORK
import com.mindmemo.domain.usecase.AllNoteUseCase
import com.mindmemo.domain.usecase.GetGridSettingUseCase
import com.mindmemo.domain.usecase.SaveGridSettingUseCase
import com.mindmemo.domain.usecase.SearchUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var allNoteUseCase: AllNoteUseCase
    private lateinit var searchUseCase: SearchUseCase
    private lateinit var getGridSettingUseCase: GetGridSettingUseCase
    private lateinit var saveGridSettingUseCase: SaveGridSettingUseCase

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        allNoteUseCase = mockk()
        searchUseCase = mockk()
        getGridSettingUseCase = mockk()
        saveGridSettingUseCase = mockk()

        every { getGridSettingUseCase.invoke() } returns MutableStateFlow(true)

        viewModel = HomeViewModel(
            allNoteUseCase,
            searchUseCase,
            getGridSettingUseCase,
            saveGridSettingUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observeGridSetting should update isGridView based on flow`() = runTest {
        val gridFlow = MutableStateFlow(false)
        every { getGridSettingUseCase.invoke() } returns gridFlow

        val viewModel = HomeViewModel(
            allNoteUseCase,
            searchUseCase,
            getGridSettingUseCase,
            saveGridSettingUseCase
        )
        advanceUntilIdle()

        assertFalse(viewModel.isGridView.value)
    }

    @Test
    fun `toggleGridView should save and update isGridView`() = runTest {
        coEvery { saveGridSettingUseCase(true) } just Runs

        viewModel.toggleGridView(true)
        advanceUntilIdle()

        assertTrue(viewModel.isGridView.value)
        coVerify { saveGridSettingUseCase(true) }
    }

    @Test
    fun `getAll should collect notes and update getAllNotes`() = runTest {
        val notes = listOf(MemoEntity(1, "title", "desc", HEALTH, HIGH, "2024-01-01"))
        coEvery { allNoteUseCase.getAllNote() } returns flowOf(notes.toMutableList())

        viewModel.getAll()
        advanceUntilIdle()

        val result = viewModel.getAllNotes.value
        assertEquals(DataStatus.success(notes, false), result)
    }

    @Test
    fun `searchNote should collect and update searchNotes`() = runTest {
        val query = "test"
        val notes = listOf(MemoEntity(1, "title", "test desc", WORK, LOW, "2025-01-02"))
        coEvery { searchUseCase.searchNote(query) } returns flowOf(notes.toMutableList())


        viewModel.searchNote(query)
        advanceUntilIdle()

        val result = viewModel.searchNotes.value
        assertEquals(DataStatus.success(notes, false), result)
    }
}