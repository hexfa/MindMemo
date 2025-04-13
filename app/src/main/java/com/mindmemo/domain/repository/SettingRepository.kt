package com.mindmemo.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    val isDarkTheme: Flow<Boolean>
    val isGridView: Flow<Boolean>
    suspend fun saveTheme(isDark: Boolean)
    suspend fun saveGrid(isGridView: Boolean)
}