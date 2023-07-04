package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun updateNote(noteEntity: MemoEntity) = noteRepository.updateNote(noteEntity)
}