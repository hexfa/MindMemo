package com.mindmemo.presentation.viewmodel

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DataStatus
import com.mindmemo.domain.usecase.AllNoteUseCase
import com.mindmemo.domain.usecase.GetGridSettingUseCase
import com.mindmemo.domain.usecase.GetThemeSettingUseCase
import com.mindmemo.domain.usecase.SaveGridSettingUseCase
import com.mindmemo.domain.usecase.SaveThemeSettingUseCase
import com.mindmemo.domain.usecase.SearchUseCase
import com.mindmemo.presentation.base.ViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val allNoteUseCase: AllNoteUseCase,
    private val searchUseCase: SearchUseCase,
    private val getGridSettingUseCase: GetGridSettingUseCase,
    private val saveGridSettingUseCase: SaveGridSettingUseCase,
) : ViewModelBase() {

    private val _getAllNotes = MutableStateFlow<DataStatus<List<MemoEntity>>?>(null)
    val getAllNotes = _getAllNotes.asStateFlow()

    private val _searchNotes = MutableStateFlow<DataStatus<List<MemoEntity>>?>(null)
    val searchNotes = _searchNotes.asStateFlow()

    private val _isGridView = MutableStateFlow(true)
    val isGridView = _isGridView.asStateFlow()

    init {
        observeGridSetting()
    }

    private fun observeGridSetting() = launchWithState {
        getGridSettingUseCase().collect { isGrid ->
            _isGridView.value = isGrid
        }
    }

    fun toggleGridView(isGrid: Boolean) = launchWithState {
        saveGridSettingUseCase(isGrid)
        _isGridView.value = isGrid
    }

    fun getAll() = launchWithState {
        allNoteUseCase.getAllNote().collect {
            _getAllNotes.value = DataStatus.success(it, it.isEmpty())
        }
    }

    fun searchNote(search: String) = launchWithState {
        searchUseCase.searchNote(search).collect {
            _searchNotes.value = DataStatus.success(it, it.isEmpty())
        }
    }
}
