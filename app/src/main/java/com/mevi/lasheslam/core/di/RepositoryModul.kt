package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.data.CourseRequestRepositoryImpl
import com.mevi.lasheslam.data.CoursesRepositoryImpl
import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.data.FavoritesRepositoryImpl
import com.mevi.lasheslam.data.UserRepositoryImpl
import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import com.mevi.lasheslam.domain.repository.UserRepository
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
    /*
    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ) : UserRepository

    @Binds
    abstract fun bindCourseRequestRepository(
        impl: CourseRequestRepositoryImpl
    ) : CourseRequestRepository

    @Binds
    abstract fun bindCoursesRepository(
        impl: CoursesRepositoryImpl
    ) : CoursesRepository

    @Binds
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ) : FavoritesRepository

     */
}