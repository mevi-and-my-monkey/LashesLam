package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModul {

    @Binds
    abstract fun bindUserPreferencesRepository(
        impl: DataStoreRepository
    ): UserPreferencesRepository
}