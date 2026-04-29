package com.mevi.lasheslam.core.di

import com.mevi.lasheslam.data.notifications.NotificationSchedulerImpl
import com.mevi.lasheslam.domain.repository.NotificationScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    abstract fun bindNotificationScheduler(
        impl: NotificationSchedulerImpl
    ): NotificationScheduler
}