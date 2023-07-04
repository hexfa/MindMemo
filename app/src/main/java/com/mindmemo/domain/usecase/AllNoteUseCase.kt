package com.example.noteappcleanarchitecture.domain.usecase

import com.example.noteappcleanarchitecture.domain.repository.HomeRepository
import javax.inject.Inject

class AllNoteUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    fun getAllNote() = homeRepository.getAllNote()
}