package com.mindmemo.presentation.viewmodel

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.HEALTH
import com.mindmemo.data.utils.LOW
import com.mindmemo.domain.usecase.DeleteUseCase
import com.mindmemo.domain.usecase.DetailUseCase
import com.mindmemo.domain.usecase.SaveUseCase
import com.mindmemo.domain.usecase.UpdateUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    private val saveUseCase: SaveUseCase = mockk()
    private val updateUseCase: UpdateUseCase = mockk()
    private val detailUseCase: DetailUseCase = mockk()
    private val deleteUseCase: DeleteUseCase = mockk()
    private lateinit var viewModel: NoteViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NoteViewModel(saveUseCase, updateUseCase, detailUseCase, deleteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onTitleChanged updates title`() = runTest {
        val newTitle = "New Title"
        viewModel.onTitleChanged(newTitle)
        assertEquals(newTitle, viewModel.detailNote.value.title)
    }

    @Test
    fun `onDescriptionChanged updates description`() = runTest {
        val newDesc = "Description"
        viewModel.onDescriptionChanged(newDesc)
        assertEquals(newDesc, viewModel.detailNote.value.description)
    }

    @Test
    fun `onPrioritySelected updates priority`() = runTest {
        viewModel.onPrioritySelected("HIGH")
        assertEquals("HIGH", viewModel.detailNote.value.priority)
    }

    @Test
    fun `onCategorySelected updates category`() = runTest {
        viewModel.onCategorySelected("WORK")
        assertEquals("WORK", viewModel.detailNote.value.category)
    }

    @Test
    fun `saveNote calls SaveUseCase and updates note id`() = runTest {
        coEvery { saveUseCase.saveNote(any()) } returns 123L
        viewModel.saveNote()
        assertEquals(123, viewModel.detailNote.value.id)
        coVerify { saveUseCase.saveNote(any()) }
    }

    @Test
    fun `updateNote calls UpdateUseCase`() = runTest {
        coEvery { updateUseCase.updateNote(any()) } just Runs
        viewModel.updateNote()
        coVerify { updateUseCase.updateNote(any()) }
    }

    @Test
    fun `getDetail loads note detail and sets state`() = runTest {
        val fakeNote = MemoEntity(1, "title", "desc", HEALTH, LOW, "2024-04-21")
        every { detailUseCase.detailNote(1) } returns flowOf(fakeNote)

        viewModel.getDetail(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(fakeNote.title, viewModel.detailNote.value.title)
    }

    @Test
    fun `deleteNote calls DeleteUseCase`() = runTest {
        coEvery { deleteUseCase.deleteNote(1) } just Runs
        viewModel.deleteNote(1)
        coVerify { deleteUseCase.deleteNote(1) }
    }
}