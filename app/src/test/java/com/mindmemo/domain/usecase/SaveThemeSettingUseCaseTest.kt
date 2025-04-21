package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.SettingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveThemeSettingUseCaseTest {

    private lateinit var settingRepository: SettingRepository
    private lateinit var saveThemeSettingUseCase: SaveThemeSettingUseCase

    @Before
    fun setUp() {
        settingRepository = mockk()
        saveThemeSettingUseCase = SaveThemeSettingUseCase(settingRepository)
    }

    @Test
    fun `invoke should call saveTheme with correct parameter`() = runTest {
        // Arrange
        val isDark = true
        coEvery { settingRepository.saveTheme(isDark) } returns Unit

        // Act
        saveThemeSettingUseCase(isDark)

        // Assert
        coVerify(exactly = 1) { settingRepository.saveTheme(isDark) }
    }
}