package com.crunchquest.android.presentation.di

import android.app.Application
import android.content.Context
import com.crunchquest.android.data.repository.UserRepositoryImpl
import com.crunchquest.android.data.source.local.UserLocalDataSource
import com.crunchquest.android.data.source.remote.ApiConfig
import com.crunchquest.android.data.remote.api.ApiService
import com.crunchquest.android.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiConfig.retrofitDbApi.create(ApiService::class.java)
    }
}
