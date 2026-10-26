package com.briefly.domain.usecases.homeUsecases

import com.briefly.domain.model.Article
import com.briefly.domain.repository.HomeRepository
import javax.inject.Inject

class GetTeslaUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    suspend operator fun invoke(): Result<List<Article>> = homeRepository.getTeslaNews()
}