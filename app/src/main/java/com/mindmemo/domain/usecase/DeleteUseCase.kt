package com.mindmemo.domain.usecase

import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.domain.repository.HomeRepository
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun deleteNote(noteEntity: MemoEntity) = homeRepository.deleteNote(noteEntity)
}