package com.mindmemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DataStatus
import com.mindmemo.domain.usecase.AllNoteUseCase
import com.mindmemo.domain.usecase.DeleteUseCase
import com.mindmemo.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val allNoteUseCase: AllNoteUseCase,
    private val searchUseCase: SearchUseCase,
    private val deleteUseCase: DeleteUseCase
) : ViewModel() {

    private val _getAllNotes: MutableStateFlow<DataStatus<List<MemoEntity>>?> =
        MutableStateFlow(null)
    val getAllNotes = _getAllNotes.asStateFlow()

    private val _searchNotes: MutableStateFlow<DataStatus<List<MemoEntity>>?> =
        MutableStateFlow(null)
    val searchNotes = _searchNotes.asStateFlow()

    fun getAll() = viewModelScope.launch {
        allNoteUseCase.getAllNote().collect {
            _getAllNotes.value = DataStatus.success(it, it.isEmpty())
        }
    }

    fun searchNote(search: String) = viewModelScope.launch {
        searchUseCase.searchNote(search).collect {
            _searchNotes.value = DataStatus.success(it, it.isEmpty())
        }
    }

    fun deleteNote(entity: MemoEntity) = viewModelScope.launch {
        deleteUseCase.deleteNote(entity)
    }

}