package com.mindmemo.domain.usecase

import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.mindmemo.domain.repository.HomeRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun deleteNote(noteEntity: NoteEntity) = homeRepository.deleteNote(noteEntity)
}