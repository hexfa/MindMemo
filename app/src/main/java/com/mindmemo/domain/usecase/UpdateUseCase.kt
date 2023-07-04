package com.example.noteappcleanarchitecture.domain.usecase

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.domain.repository.HomeRepository
import com.example.noteappcleanarchitecture.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    suspend fun updateNote(noteEntity: NoteEntity) = noteRepository.updateNote(noteEntity)
}