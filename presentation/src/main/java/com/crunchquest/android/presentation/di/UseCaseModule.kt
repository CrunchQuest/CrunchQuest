package com.crunchquest.android.presentation.di

import com.crunchquest.android.domain.repository.BookingRepository
import com.crunchquest.android.domain.repository.RequestRepository
import com.crunchquest.android.domain.repository.ServiceRepository
import com.crunchquest.android.domain.repository.UserRepository
import com.crunchquest.android.domain.usecase.booking.CreateBookingUseCase
import com.crunchquest.android.domain.usecase.request.CreateRequestUseCase
import com.crunchquest.android.domain.usecase.request.GetRequestDetailsUseCase
import com.crunchquest.android.domain.usecase.service.GetServiceUseCase
import com.crunchquest.android.domain.usecase.user.GetUserUseCase
import com.crunchquest.android.domain.usecase.user.LoginUseCase
import com.crunchquest.android.domain.usecase.user.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // User Use Cases
    @Provides
    @Singleton
    fun provideLoginUseCase(userRepository: UserRepository): LoginUseCase {
        return LoginUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(userRepository: UserRepository): RegisterUserUseCase {
        return RegisterUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    // Request Use Cases
    @Provides
    @Singleton
    fun provideCreateRequestUseCase(requestRepository: RequestRepository): CreateRequestUseCase {
        return CreateRequestUseCase(requestRepository)
    }

    @Provides
    @Singleton
    fun provideGetRequestDetailsUseCase(requestRepository: RequestRepository): GetRequestDetailsUseCase {
        return GetRequestDetailsUseCase(requestRepository)
    }

    // Service Use Cases
    @Provides
    @Singleton
    fun provideBookingServiceUseCase(bookingRepository: BookingRepository): CreateBookingUseCase {
        return CreateBookingUseCase(bookingRepository)
    }

    @Provides
    @Singleton
    fun provideGetServiceUseCase(serviceRepository: ServiceRepository): GetServiceUseCase {
        return GetServiceUseCase(serviceRepository)
    }
}
