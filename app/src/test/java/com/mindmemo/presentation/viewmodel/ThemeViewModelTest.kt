package com.mindmemo.presentation.viewmodel

import com.mindmemo.domain.usecase.GetThemeSettingUseCase
import com.mindmemo.domain.usecase.SaveThemeSettingUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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
class ThemeViewModelTest {

    private val getThemeSettingUseCase: GetThemeSettingUseCase = mockk()
    private val saveThemeSettingUseCase: SaveThemeSettingUseCase = mockk()
    private lateinit var viewModel: ThemeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should emit theme value from use case`() = runTest {
        // Given
        coEvery { getThemeSettingUseCase.invoke() } returns flowOf(true)

        // When
        viewModel = ThemeViewModel(getThemeSettingUseCase, saveThemeSettingUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(true, viewModel.isDarkTheme.value)
    }

    @Test
    fun `toggleTheme should save theme and update state`() = runTest {
        // Given
        coEvery { getThemeSettingUseCase.invoke() } returns flowOf(false)
        coEvery { saveThemeSettingUseCase.invoke(true) } returns Unit

        viewModel = ThemeViewModel(getThemeSettingUseCase, saveThemeSettingUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.toggleTheme(true)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { saveThemeSettingUseCase(true) }
        assertEquals(true, viewModel.isDarkTheme.value)
    }
}