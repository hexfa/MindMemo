package com.mindmemo.domain.usecase


import com.mindmemo.domain.repository.HomeRepository
import javax.inject.Inject

class AllNoteUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    fun getAllNote() = homeRepository.getAllNote()
}