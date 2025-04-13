package com.mindmemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmemo.domain.usecase.GetThemeSettingUseCase
import com.mindmemo.domain.usecase.SaveThemeSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val getThemeSettingUseCase: GetThemeSettingUseCase,
    private val saveThemeSettingUseCase: SaveThemeSettingUseCase
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    init {
        viewModelScope.launch {
            getThemeSettingUseCase().collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            saveThemeSettingUseCase(isDark)
            _isDarkTheme.value = isDark
        }
    }
}