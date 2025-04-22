package com.mindmemo.data.repository

import app.cash.turbine.test
import com.mindmemo.data.datastore.SettingDataSource
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

class SettingRepositoryImplTest {

    private lateinit var repository: SettingRepositoryImp
    private val mockSettingDataSource: SettingDataSource = mockk()

    @Before
    fun setUp() {
        repository = SettingRepositoryImp(mockSettingDataSource)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `saveTheme should call SettingDataSource with correct value`() = runTest {
        // Given
        val isDark = true
        coEvery { mockSettingDataSource.saveTheme(isDark) } just Runs

        // When
        repository.saveTheme(isDark)

        // Then
        coVerify(exactly = 1) { mockSettingDataSource.saveTheme(isDark) }
    }

    @Test
    fun `saveGrid should call SettingDataSource with correct value`() = runTest {
        // Given
        val isGrid = false
        coEvery { mockSettingDataSource.saveGridView(isGrid) } just Runs

        // When
        repository.saveGrid(isGrid)

        // Then
        coVerify(exactly = 1) { mockSettingDataSource.saveGridView(isGrid) }
    }

    @Test
    fun `isDarkTheme should emit correct value`() = runTest {
        // Given
        val expected = true
        every { mockSettingDataSource.isDarkTheme } returns flowOf(expected)

        // When & Then
        repository.isDarkTheme.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) { mockSettingDataSource.isDarkTheme }
    }

    @Test
    fun `isGridView should emit correct value`() = runTest {
        // Given
        val expected = false
        every { mockSettingDataSource.isGridView } returns flowOf(expected)

        // When & Then
        repository.isGridView.test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) { mockSettingDataSource.isGridView }
    }
}