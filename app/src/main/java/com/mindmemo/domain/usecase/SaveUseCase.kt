package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.domain.repository.NoteRepository
import javax.inject.Inject

class SaveUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun saveNote(noteEntity: MemoEntity) = noteRepository.saveNote(noteEntity)
}