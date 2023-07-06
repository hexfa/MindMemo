package com.mindmemo.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val saveUseCase: SaveUseCase,
    private val updateUseCase: UpdateUseCase,
    private val detailUseCase: DetailUseCase
) : ViewModel() {
    //Spinners
    val categoriesList = MutableLiveData<MutableList<String>>()
    val prioritiesList = MutableLiveData<MutableList<String>>()
    private val _detailNote : MutableStateFlow<DataStatus<MemoEntity>?> = MutableStateFlow(null)
    val detailNote = _detailNote.asStateFlow()



    fun loadCategoriesData() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(WORK, EDUCATION, HOME, HEALTH)
        categoriesList.postValue(data)
    }

    fun loadPrioritiesData() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(HIGH, NORMAL, LOW)
        prioritiesList.postValue(data)
    }

    fun saveOrEditNote(isEdit : Boolean ,entity: MemoEntity) = viewModelScope.launch {
        if (isEdit){
            updateUseCase.updateNote(entity)
        }else{
            saveUseCase.saveNote(entity)
        }
    }

    fun getDetail(id : Int) = viewModelScope.launch {
        detailUseCase.detailNote(id).collect{
            _detailNote.value = DataStatus.success(it,false)
        }
    }

}