package com.mindmemo.presentation.viewmodel

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DataStatus
import com.mindmemo.domain.usecase.AllNoteUseCase
import com.mindmemo.domain.usecase.GetGridSettingUseCase
import com.mindmemo.domain.usecase.SaveGridSettingUseCase
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

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    private val _filteredNotes = MutableStateFlow<List<MemoEntity>>(emptyList())
    val filteredNotes = _filteredNotes.asStateFlow()

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
        allNoteUseCase.getAllNote().collect { allNotes ->
            _getAllNotes.value = DataStatus.success(allNotes, allNotes.isEmpty())

            val allCategories = allNotes
                .map { it.category.trim() }
                .filter { it.isNotBlank() }
                .distinct()

            _categories.value = allCategories

            val maxTabIndex = allCategories.size // چون تب 0 همه هست
            if (_selectedTabIndex.value > maxTabIndex) {
                _selectedTabIndex.value = 0
            }

            filterNotesByTab(_selectedTabIndex.value, allNotes)
        }
    }

    fun searchNote(search: String) = launchWithState {
        searchUseCase.searchNote(search).collect {
            _searchNotes.value = DataStatus.success(it, it.isEmpty())
        }
    }

    fun onTabSelected(index: Int) {
        val maxTabIndex = _categories.value.size
        if (index > maxTabIndex || index < 0) {
            _selectedTabIndex.value = 0
        } else {
            _selectedTabIndex.value = index
        }

        val currentNotes = _getAllNotes.value?.data ?: return
        filterNotesByTab(_selectedTabIndex.value, currentNotes)
    }

    private fun filterNotesByTab(index: Int, notes: List<MemoEntity>) {
        _filteredNotes.value = if (index == 0) {
            notes
        } else {
            val category = _categories.value.getOrNull(index - 1)
            if (category == null) {
                emptyList()
            } else {
                notes.filter { it.category == category }
            }
        }
    }

//    private fun syncSelectedTabIndexWithCategories() {
//        val maxIndex = _categories.value.size
//        if (_selectedTabIndex.value > maxIndex) {
//            _selectedTabIndex.value = 0
//        }
//    }
}