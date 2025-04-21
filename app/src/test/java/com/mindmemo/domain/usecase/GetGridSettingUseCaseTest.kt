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

class GetGridSettingUseCaseTest {

    private lateinit var settingRepository: SettingRepository
    private lateinit var getGridSettingUseCase: GetGridSettingUseCase

    @Before
    fun setUp() {
        settingRepository = mockk()
        getGridSettingUseCase = GetGridSettingUseCase(settingRepository)
    }

    @Test
    fun `invoke should return expected grid view setting`() = runTest {
        // Arrange
        val expectedGridView = true
        every { settingRepository.isGridView } returns flowOf(expectedGridView)

        // Act
        val result = getGridSettingUseCase().first()

        // Assert
        assertEquals(expectedGridView, result)
        verify(exactly = 1) { settingRepository.isGridView }
    }
}