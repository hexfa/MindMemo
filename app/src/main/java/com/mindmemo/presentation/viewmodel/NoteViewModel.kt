package com.mindmemo.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DataStatus
import com.mindmemo.data.utils.EDUCATION
import com.mindmemo.data.utils.HEALTH
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.HOME
import com.mindmemo.data.utils.LOW
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK
import com.mindmemo.domain.usecase.DetailUseCase
import com.mindmemo.domain.usecase.SaveUseCase
import com.mindmemo.domain.usecase.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.mindmemo.domain.usecase.DeleteUseCase
import com.mindmemo.presentation.base.ViewModelBase
import com.mindmemo.presentation.notification.NotificationService

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val saveUseCase: SaveUseCase,
    private val updateUseCase: UpdateUseCase,
    private val detailUseCase: DetailUseCase,
    private val deleteUseCase: DeleteUseCase
) : ViewModelBase() {

    val categoriesList = MutableLiveData<MutableList<String>>()
    val prioritiesList = mutableListOf(HIGH, NORMAL, LOW)
    private val _detailNote: MutableStateFlow<DataStatus<MemoEntity>?> = MutableStateFlow(null)
    val detailNote = _detailNote.asStateFlow()

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var priority by mutableStateOf(NORMAL)
        private set

    fun onTitleChanged(newTitle: String) {
        if (newTitle.count { it == '\n' } < 2) {
            title = newTitle
        }
    }

    fun onDescriptionChanged(newDescription: String) {
        description = newDescription
    }

    fun onPrioritySelected(newPriority: String) {
        priority = newPriority
    }

    fun loadCategoriesData() = launchWithDispatchers {
        categoriesList.postValue(mutableListOf(WORK, EDUCATION, HOME, HEALTH))
    }

    fun saveNote() = launchWithState {
        if (description.isEmpty()) NotificationService.showError("Please Enter Description")
        else saveUseCase.saveNote(
            MemoEntity(
                title = title,
                disc = description,
                priority = priority
            )
        )
    }

    fun updateNote(id: Int) = launchWithState {
        updateUseCase.updateNote(
            MemoEntity(
                id = id,
                title = title,
                disc = description,
                priority = priority
            )
        )
    }

    fun getDetail(id: Int) = viewModelScope.launch {
        detailUseCase.detailNote(id).collect { memo ->
            if (memo != null) {
                _detailNote.value = DataStatus.success(memo, false)
                title = memo.title ?: ""
                description = memo.disc
                priority = memo.priority
            }
        }
    }

    fun deleteNote(noteId: Int) = viewModelScope.launch {
        deleteUseCase.deleteNote(noteId)
        title = ""
        description = ""
        priority = NORMAL
    }
}