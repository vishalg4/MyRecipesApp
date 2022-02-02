package com.app.di

import com.app.data.error.mapper.ErrorMapper
import com.app.data.error.mapper.ErrorMapperSource
import com.app.usecase.errors.ErrorUseCase
import com.app.usecase.errors.ErrorManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This [ErrorModule] defines the dependencies which are requited error handling classes.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorUseCase

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperSource
}
