package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.core.error.ErrorMapper
import com.mevi.lasheslam.data.error.FirebaseErrorMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindErrorMapper(
        impl: FirebaseErrorMapper
    ): ErrorMapper
}