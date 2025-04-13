package com.mindmemo.data.repository

import com.mindmemo.data.datastore.SettingDataSource
import com.mindmemo.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImp(private val settingDataSource: SettingDataSource) : SettingRepository {

    override val isDarkTheme: Flow<Boolean> = settingDataSource.isDarkTheme
    override val isGridView: Flow<Boolean> = settingDataSource.isGridView

    override suspend fun saveTheme(isDark: Boolean) {
        settingDataSource.saveTheme(isDark)
    }

    override suspend fun saveGrid(isGridView: Boolean) {
        settingDataSource.saveGridView(isGridView)
    }
}