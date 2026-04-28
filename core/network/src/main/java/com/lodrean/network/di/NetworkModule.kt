package com.lodrean.network.di

import com.lodrean.network.StepikApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideStepikApiService(): StepikApiService {
        return StepikApiService()
    }
}
