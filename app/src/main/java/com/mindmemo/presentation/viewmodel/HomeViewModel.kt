package com.mindmemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DataStatus
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.LOW
import com.mindmemo.data.utils.NORMAL
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

    //todo remove
    //test data
    init {
        val initialNotes = listOf(
            MemoEntity(id = 1, title = "Title 1", disc = "", category = "", priority = NORMAL),
            MemoEntity(
                id = 2,
                title = "Title 2",
                disc = "Description for note 2  Description for note 2",
                category = "",
                priority = LOW
            ),
            MemoEntity(
                id = 3,
                title = "Title 3",
                disc = "Description for note 3",
                category = "",
                priority = HIGH
            ),
            MemoEntity(
                id = 4,
                title = "Title 4",
                disc = "Description for note 4",
                category = "",
                priority = LOW
            ),
            MemoEntity(
                id = 5,
                title = "Title 5",
                disc = "Description for note 5",
                category = "",
                priority = NORMAL
            )
        )
        _getAllNotes.value = DataStatus.success(initialNotes, false)
    }

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