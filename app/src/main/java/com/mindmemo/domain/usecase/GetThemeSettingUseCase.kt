package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeSettingUseCase @Inject constructor(
    private val repository: SettingRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isDarkTheme
}