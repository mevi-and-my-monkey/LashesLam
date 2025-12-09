package com.mevi.lasheslam.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.CourseRequestRepositoryImpl
import com.mevi.lasheslam.data.UserRepositoryImpl
import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): UserRepository = UserRepositoryImpl(auth, firestore)

    @Provides
    fun provideCourseRequestRepository(
        firestore: FirebaseFirestore
    ): CourseRequestRepository = CourseRequestRepositoryImpl(firestore)
}