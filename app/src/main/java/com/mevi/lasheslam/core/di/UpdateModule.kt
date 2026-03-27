package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.data.PlayCoreUpdateRepository
import com.mevi.lasheslam.domain.repository.UpdateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UpdateModule {

    @Binds
    abstract fun bindUpdateRepository(
        impl: PlayCoreUpdateRepository
    ): UpdateRepository
}