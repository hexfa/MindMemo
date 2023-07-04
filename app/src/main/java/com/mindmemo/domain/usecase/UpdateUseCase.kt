package com.example.noteappcleanarchitecture.domain.usecase

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.mindmemo.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun updateNote(noteEntity: NoteEntity) = noteRepository.updateNote(noteEntity)
}