package com.example.noteappcleanarchitecture.domain.usecase

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.domain.repository.NoteRepository
import javax.inject.Inject

class SaveUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun saveNote(noteEntity: NoteEntity) = noteRepository.saveNote(noteEntity)
}