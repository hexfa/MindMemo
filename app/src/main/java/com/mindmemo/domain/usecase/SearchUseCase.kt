package com.example.noteappcleanarchitecture.domain.usecase

import com.mindmemo.domain.repository.HomeRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    fun searchNote(search : String) = homeRepository.searchNotes(search)
}