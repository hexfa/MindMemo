package com.example.noteappcleanarchitecture.domain.usecase

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.domain.repository.HomeRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun deleteNote(noteEntity: NoteEntity) = homeRepository.deleteNote(noteEntity)
}