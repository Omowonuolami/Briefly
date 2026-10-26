package com.briefly.data.repository

import com.briefly.core.utils.toDomain
import com.briefly.data.api.NewsApiService
import com.briefly.data.dao.ArticleDao
import com.briefly.data.mapper.toDomain
import com.briefly.domain.model.Article
import com.briefly.domain.model.NewsResponse
import com.briefly.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val articleDao: ArticleDao
): HomeRepository {
    override fun getArticles(): Flow<List<Article>> {
        return articleDao.getAllArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTeslaNews(): Result<List<Article>> {
        return try {
            Result.success(newsApiService.getTeslaNews().articles.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAppleNews(): Result<List<Article>> {
        return try {
            Result.success(newsApiService.getAppleNews().articles.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBusinessNews(): Result<List<Article>> {
        return try {
            Result.success(newsApiService.getBusinessNews().articles.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTechCrunchNews(): Result<List<Article>> {
        return try {
            Result.success(newsApiService.getTechCrunchNews().articles.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWallStreetJournal(): Result<List<Article>> {
        return try {
            Result.success(newsApiService.getWallStreetJournal().articles.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}