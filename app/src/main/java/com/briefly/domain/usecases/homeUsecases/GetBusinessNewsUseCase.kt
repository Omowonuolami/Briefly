package com.briefly.domain.usecases.homeUsecases

import com.briefly.domain.model.Article
import com.briefly.domain.repository.HomeRepository
import javax.inject.Inject

class GetBusinessNewsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<Article>> = repository.getBusinessNews()
}