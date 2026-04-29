package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.data.CourseRequestRepositoryImpl
import com.mevi.lasheslam.data.CoursesRepositoryImpl
import com.mevi.lasheslam.data.DataStoreRepository
import com.mevi.lasheslam.data.FavoritesRepositoryImpl
import com.mevi.lasheslam.data.PlayCoreUpdateRepository
import com.mevi.lasheslam.data.SessionDataSourceImpl
import com.mevi.lasheslam.data.SessionRepositoryImpl
import com.mevi.lasheslam.data.UserCourseRepositoryImpl
import com.mevi.lasheslam.data.UserRepositoryImpl
import com.mevi.lasheslam.domain.repository.CourseRequestRepository
import com.mevi.lasheslam.domain.repository.CoursesRepository
import com.mevi.lasheslam.domain.repository.FavoritesRepository
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.domain.repository.UpdateRepository
import com.mevi.lasheslam.domain.repository.UserCourseRepository
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import com.mevi.lasheslam.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserPreferencesRepository(
        impl: DataStoreRepository
    ): UserPreferencesRepository

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindCourseRequestRepository(
        impl: CourseRequestRepositoryImpl
    ): CourseRequestRepository

    @Binds
    abstract fun bindCoursesRepository(
        impl: CoursesRepositoryImpl
    ): CoursesRepository

    @Binds
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    abstract fun bindUpdateRepository(
        impl: PlayCoreUpdateRepository
    ): UpdateRepository

    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    abstract fun bindSessionDataSource(
        impl: SessionDataSourceImpl
    ) : SessionDataSource


    @Binds
    abstract fun bindUserCourseRepository(
        impl: UserCourseRepositoryImpl
    ): UserCourseRepository
}