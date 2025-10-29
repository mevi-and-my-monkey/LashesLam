package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.domain.repository.UserRepository
import com.mevi.lasheslam.domain.usecase.GetIsDarkModeUseCase
import com.mevi.lasheslam.domain.usecase.LoginUseCase
import com.mevi.lasheslam.domain.usecase.RegisterUseCase
import com.mevi.lasheslam.domain.usecase.SaveIsDarkModeUseCase
import com.mevi.lasheslam.domain.usecase.SignInWithGoogleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoginUseCase(repo: UserRepository): LoginUseCase =
        LoginUseCase(repo)

    @Provides
    fun provideRegisterUseCase(repo: UserRepository): RegisterUseCase =
        RegisterUseCase(repo)

    @Provides
    fun provideSignInWithGoogleUseCase(repo: UserRepository): SignInWithGoogleUseCase =
        SignInWithGoogleUseCase(repo)

    @Provides
    fun provideSaveIsDarkModeUseCase(repo: DataStoreRepository) = SaveIsDarkModeUseCase(repo)

    @Provides
    fun provideGetIsDarkModeUseCase(repo: DataStoreRepository) = GetIsDarkModeUseCase(repo)
}