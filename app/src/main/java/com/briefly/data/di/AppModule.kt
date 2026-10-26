package com.briefly.data.di

import com.briefly.data.api.NewsApiService
import com.briefly.data.dao.ArticleDao
import com.briefly.data.repository.HomeRepositoryImpl
import com.briefly.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //Retrofit -> HttpLogging
    //API service -> Retrofit
    //RepositoryImpl; -> APi service
    //Repository impl -> Repository (interface)

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor (HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NewsApiService{
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepositoryImpl(newsApiService: NewsApiService, articleDao: ArticleDao): HomeRepository {
        return HomeRepositoryImpl(
            newsApiService, articleDao
        )
    }
}