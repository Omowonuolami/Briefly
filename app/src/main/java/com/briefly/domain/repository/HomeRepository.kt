package com.briefly.domain.repository

import com.briefly.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getArticles(): Flow<List<Article>>  // ← single method, returns Flow
    suspend fun getTeslaNews(): Result<List<Article>>
    suspend fun getAppleNews(): Result<List<Article>>
    suspend fun getBusinessNews(): Result<List<Article>>
    suspend fun getTechCrunchNews(): Result<List<Article>>
    suspend fun getWallStreetJournal(): Result<List<Article>>
}