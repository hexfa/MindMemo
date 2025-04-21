package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.SettingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveGridSettingUseCaseTest {

    private lateinit var settingRepository: SettingRepository
    private lateinit var saveGridSettingUseCase: SaveGridSettingUseCase

    @Before
    fun setUp() {
        settingRepository = mockk()
        saveGridSettingUseCase = SaveGridSettingUseCase(settingRepository)
    }

    @Test
    fun `invoke should call saveGrid with correct parameter`() = runTest {
        // Arrange
        val isGrid = true
        coEvery { settingRepository.saveGrid(isGrid) } returns Unit

        // Act
        saveGridSettingUseCase(isGrid)

        // Assert
        coVerify(exactly = 1) { settingRepository.saveGrid(isGrid) }
    }
}