package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.HomeRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun deleteNote(noteId: Int) = homeRepository.deleteNote(noteId = noteId)
}