package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.SettingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetThemeSettingUseCaseTest {

    private lateinit var settingRepository: SettingRepository
    private lateinit var getThemeSettingUseCase: GetThemeSettingUseCase

    @Before
    fun setUp() {
        settingRepository = mockk()
        getThemeSettingUseCase = GetThemeSettingUseCase(settingRepository)
    }

    @Test
    fun `invoke should return expected dark theme setting`() = runTest {
        // Arrange
        val expectedDarkTheme = true
        every { settingRepository.isDarkTheme } returns flowOf(expectedDarkTheme)

        // Act
        val result = getThemeSettingUseCase().first()

        // Assert
        assertEquals(expectedDarkTheme, result)
        verify(exactly = 1) { settingRepository.isDarkTheme }
    }
}