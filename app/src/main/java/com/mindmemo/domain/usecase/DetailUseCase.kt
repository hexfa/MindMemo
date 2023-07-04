package com.example.noteappcleanarchitecture.domain.usecase

import com.example.noteappcleanarchitecture.domain.repository.NoteRepository
import javax.inject.Inject

class DetailUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    fun detailNote(id:Int) = noteRepository.detailNote(id)
}