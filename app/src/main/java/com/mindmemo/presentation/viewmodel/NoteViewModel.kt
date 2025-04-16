package com.mindmemo.presentation.viewmodel

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.EDUCATION
import com.mindmemo.data.utils.HEALTH
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.HOME
import com.mindmemo.data.utils.LOW
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK
import com.mindmemo.domain.usecase.DeleteUseCase
import com.mindmemo.domain.usecase.DetailUseCase
import com.mindmemo.domain.usecase.SaveUseCase
import com.mindmemo.domain.usecase.UpdateUseCase
import com.mindmemo.presentation.base.ViewModelBase
import com.mindmemo.presentation.notification.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val saveUseCase: SaveUseCase,
    private val updateUseCase: UpdateUseCase,
    private val detailUseCase: DetailUseCase,
    private val deleteUseCase: DeleteUseCase
) : ViewModelBase() {

    private var initNote: MemoEntity? = null
    private val _hasUnsavedChanges = MutableStateFlow(false)
    val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges
    val categoriesList = mutableListOf(HOME, WORK, EDUCATION, HEALTH)
    val prioritiesList = mutableListOf(HIGH, NORMAL, LOW)
    private val _detailNote: MutableStateFlow<MemoEntity> =
        MutableStateFlow(
            MemoEntity(
                0,
                "",
                "",
                HOME,
                NORMAL,
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
        )
    val detailNote = _detailNote.asStateFlow()

    init {
        launchWithState {
            detailNote.collect { note ->
                _hasUnsavedChanges.value = hasChanges()
            }
        }
    }

    fun onTitleChanged(newTitle: String) {
        if (newTitle.count { it == '\n' } < 2) {
            updateNote(title = newTitle)
        }
    }

    fun onDescriptionChanged(newDescription: String) {
        updateNote(description = newDescription)
    }

    fun onPrioritySelected(newPriority: String) {
        updateNote(priority = newPriority)
    }

    fun onCategorySelected(newCategory: String) {
        updateNote(category = newCategory)
    }

    fun saveNote() = launchWithState {
        if (detailNote.value.description.isEmpty()) NotificationService.showError("Please Enter Description")
        else {
            val createId: Long = saveUseCase.saveNote(detailNote.value)
            updateNote(id = createId.toInt())
        }
    }

    fun updateNote() = launchWithState {
        updateUseCase.updateNote(
            detailNote.value
        )
    }

    fun getDetail(id: Int) = launchWithState {
        detailUseCase.detailNote(id).collect { memo ->
            if (memo != null) {
                initNote = memo
                _detailNote.value = memo.copy()
            }
        }
    }

    fun deleteNote(noteId: Int) = launchWithState {
        deleteUseCase.deleteNote(noteId)
//        updateNote()
    }

    private fun updateNote(
        id: Int? = null,
        title: String? = null,
        description: String? = null,
        priority: String? = null,
        category: String? = null,
        date: String? = null
    ) {
        val current = _detailNote.value
        _detailNote.value = current.copy(
            id = id ?: current.id,
            title = title ?: current.title,
            description = description ?: current.description,
            priority = priority ?: current.priority,
            category = category ?: current.category,
            dateCreated = date ?: current.dateCreated
        )
    }

    private fun hasChanges(): Boolean {
        return detailNote.value.title != initNote?.title ||
                detailNote.value.description != initNote?.description ||
                detailNote.value.priority != initNote?.priority ||
                detailNote.value.category != initNote?.category
    }
}