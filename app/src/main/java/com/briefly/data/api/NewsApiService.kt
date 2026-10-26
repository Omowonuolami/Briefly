package com.briefly.data.api

import com.briefly.domain.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface NewsApiService {

    @GET("v2/everything?q=tesla&from=2026-02-28&sortBy=publishedAt&apiKey=57d438758a684bc1aed9c43667913cba")
    suspend fun getTeslaNews(): NewsResponse

    @GET("v2/everything?q=apple&from=2026-03-29&to=2026-03-29&sortBy=popularity&apiKey=57d438758a684bc1aed9c43667913cba")
    suspend fun getAppleNews(): NewsResponse

    @GET("v2/top-headlines?country=us&category=business&apiKey=57d438758a684bc1aed9c43667913cba")
    suspend fun getBusinessNews(): NewsResponse

    @GET("v2/top-headlines?sources=techcrunch&apiKey=57d438758a684bc1aed9c43667913cba")
    suspend fun getTechCrunchNews(): NewsResponse

    @GET("v2/everything?domains=wsj.com&apiKey=57d438758a684bc1aed9c43667913cba")
    suspend fun getWallStreetJournal(): NewsResponse
}