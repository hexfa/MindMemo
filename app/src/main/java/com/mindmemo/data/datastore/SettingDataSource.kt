package com.mindmemo.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingDataSource(private val context: Context) {

    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")
    private val GRID_KEY = booleanPreferencesKey("is_grid_view")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    val isGridView: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[GRID_KEY] ?: false
        }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    suspend fun saveGridView(isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[GRID_KEY] = isGrid
        }
    }
}