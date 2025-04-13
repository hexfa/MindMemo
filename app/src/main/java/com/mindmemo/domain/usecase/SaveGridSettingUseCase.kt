package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.SettingRepository
import javax.inject.Inject

class SaveGridSettingUseCase @Inject constructor(
    private val repository: SettingRepository
) {
    suspend operator fun invoke(isGrid: Boolean) = repository.saveGrid(isGrid)
}