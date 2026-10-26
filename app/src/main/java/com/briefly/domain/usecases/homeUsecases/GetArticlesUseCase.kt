package com.briefly.domain.usecases.homeUsecases

import com.briefly.domain.model.Article
import com.briefly.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return homeRepository.getArticles()
    }
}
