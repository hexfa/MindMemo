package com.mindmemo.domain.usecase

import com.mindmemo.domain.repository.NoteRepository
import javax.inject.Inject

class DetailUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    fun detailNote(id:Int) = noteRepository.detailNote(id)
}