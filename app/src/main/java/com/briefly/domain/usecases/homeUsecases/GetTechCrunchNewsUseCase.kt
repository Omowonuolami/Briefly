package com.briefly.domain.usecases.homeUsecases

import com.briefly.domain.model.Article
import com.briefly.domain.repository.HomeRepository
import javax.inject.Inject

class GetTechCrunchNewsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Article>> = repository.getTechCrunchNews()
}