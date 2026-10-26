package com.briefly.data.di

import android.app.Application
import androidx.room.Room
import com.briefly.data.database.BrieflyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBrieflyDatabase(app: Application): BrieflyDatabase {
        return Room.databaseBuilder(
            app,
            BrieflyDatabase::class.java,
            "brieflyDatabase"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides@Singleton
    fun provideArticleDao(db: BrieflyDatabase) = db.articleDao()
}
