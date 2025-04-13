package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.SettingRepository
import javax.inject.Inject

class SaveThemeSettingUseCase @Inject constructor(
    private val repository: SettingRepository
) {
    suspend operator fun invoke(isDark: Boolean) = repository.saveTheme(isDark)
}