package com.crunchquest.android.presentation.di

import com.crunchquest.android.data.mapper.UserMapper
import com.crunchquest.android.data.mapper.AssistantListMapper
import com.crunchquest.android.data.mapper.AssistantMapper
import com.crunchquest.android.data.mapper.BookingMapper
import com.crunchquest.android.data.mapper.CategoryMapper
import com.crunchquest.android.data.mapper.ChatMapper
import com.crunchquest.android.data.mapper.MessageMapper
import com.crunchquest.android.data.mapper.NotificationMapper
import com.crunchquest.android.data.mapper.PaymentMapper
import com.crunchquest.android.data.mapper.ProviderMapper
import com.crunchquest.android.data.mapper.RequestMapper
import com.crunchquest.android.data.mapper.ReviewMapper
import com.crunchquest.android.data.mapper.ServiceAvailabilityMapper
import com.crunchquest.android.data.mapper.ServiceMapper
import com.crunchquest.android.data.remote.api.ApiService
import com.crunchquest.android.data.repository.AssistantListRepositoryImpl
import com.crunchquest.android.data.repository.AssistantRepositoryImpl
import com.crunchquest.android.data.repository.BookingRepositoryImpl
import com.crunchquest.android.data.repository.CategoryRepositoryImpl
import com.crunchquest.android.data.repository.ChatRepositoryImpl
import com.crunchquest.android.data.repository.MessageRepositoryImpl
import com.crunchquest.android.data.repository.NotificationRepositoryImpl
import com.crunchquest.android.data.repository.PaymentRepositoryImpl
import com.crunchquest.android.data.repository.ProviderRepositoryImpl
import com.crunchquest.android.data.repository.RequestRepositoryImpl
import com.crunchquest.android.data.repository.ReviewRepositoryImpl
import com.crunchquest.android.data.repository.ServiceAvailabilityRepositoryImpl
import com.crunchquest.android.data.repository.ServiceRepositoryImpl
import com.crunchquest.android.data.repository.UserRepositoryImpl
import com.crunchquest.android.data.source.local.AssistantListLocalDataSource
import com.crunchquest.android.data.source.local.AssistantLocalDataSource
import com.crunchquest.android.data.source.local.BookingLocalDataSource
import com.crunchquest.android.data.source.local.CategoryLocalDataSource
import com.crunchquest.android.data.source.local.ChatLocalDataSource
import com.crunchquest.android.data.source.local.MessageLocalDataSource
import com.crunchquest.android.data.source.local.NotificationLocalDataSource
import com.crunchquest.android.data.source.local.PaymentLocalDataSource
import com.crunchquest.android.data.source.local.ProviderLocalDataSource
import com.crunchquest.android.data.source.local.RequestLocalDataSource
import com.crunchquest.android.data.source.local.ReviewLocalDataSource
import com.crunchquest.android.data.source.local.ServiceAvailabilityLocalDataSource
import com.crunchquest.android.data.source.local.ServiceLocalDataSource
import com.crunchquest.android.data.source.local.UserLocalDataSource
import com.crunchquest.android.data.source.remote.AssistantListRemoteDataSource
import com.crunchquest.android.data.source.remote.AssistantRemoteDataSource
import com.crunchquest.android.data.source.remote.BookingRemoteDataSource
import com.crunchquest.android.data.source.remote.CategoryRemoteDataSource
import com.crunchquest.android.data.source.remote.ChatRemoteDataSource
import com.crunchquest.android.data.source.remote.MessageRemoteDataSource
import com.crunchquest.android.data.source.remote.NotificationRemoteDataSource
import com.crunchquest.android.data.source.remote.PaymentRemoteDataSource
import com.crunchquest.android.data.source.remote.ProviderRemoteDataSource
import com.crunchquest.android.data.source.remote.RequestRemoteDataSource
import com.crunchquest.android.data.source.remote.ReviewRemoteDataSource
import com.crunchquest.android.data.source.remote.ServiceAvailabilityRemoteDataSource
import com.crunchquest.android.data.source.remote.ServiceRemoteDataSource
import com.crunchquest.android.data.source.remote.UserRemoteDataSource
import com.crunchquest.android.domain.repository.AssistantListRepository
import com.crunchquest.android.domain.repository.AssistantRepository
import com.crunchquest.android.domain.repository.BookingRepository
import com.crunchquest.android.domain.repository.CategoryRepository
import com.crunchquest.android.domain.repository.ChatRepository
import com.crunchquest.android.domain.repository.MessageRepository
import com.crunchquest.android.domain.repository.NotificationRepository
import com.crunchquest.android.domain.repository.PaymentRepository
import com.crunchquest.android.domain.repository.ProviderRepository
import com.crunchquest.android.domain.repository.RequestRepository
import com.crunchquest.android.domain.repository.ReviewRepository
import com.crunchquest.android.domain.repository.ServiceAvailabilityRepository
import com.crunchquest.android.domain.repository.ServiceRepository
import com.crunchquest.android.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        userLocalDataSource: UserLocalDataSource,
        userMapper: UserMapper
    ): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource, userLocalDataSource, userMapper)
    }

    @Provides
    @Singleton
    fun provideRequestRepository(
        requestRemoteDataSource: RequestRemoteDataSource,
        requestLocalDataSource: RequestLocalDataSource,
        requestMapper: RequestMapper
    ): RequestRepository {
        return RequestRepositoryImpl(requestRemoteDataSource, requestLocalDataSource, requestMapper)
    }

    @Provides
    @Singleton
    fun provideAssistantRepository(
        assistantRemoteDataSource: AssistantRemoteDataSource,
        assistantLocalDataSource: AssistantLocalDataSource,
        assistantMapper: AssistantMapper
    ): AssistantRepository {
        return AssistantRepositoryImpl(assistantRemoteDataSource, assistantLocalDataSource, assistantMapper)
    }

    @Provides
    @Singleton
    fun provideBookingRepository(
        bookingRemoteDataSource: BookingRemoteDataSource,
        bookingLocalDataSource: BookingLocalDataSource,
        bookingMapper: BookingMapper
    ): BookingRepository {
        return BookingRepositoryImpl(bookingRemoteDataSource, bookingLocalDataSource, bookingMapper)
    }

    @Provides
    @Singleton
    fun provideServiceRepository(
        serviceRemoteDataSource: ServiceRemoteDataSource,
        serviceLocalDataSource: ServiceLocalDataSource,
        serviceMapper: ServiceMapper
    ): ServiceRepository {
        return ServiceRepositoryImpl(serviceRemoteDataSource, serviceLocalDataSource, serviceMapper)
    }

    @Provides
    @Singleton
    fun provideProviderRepository(
        providerRemoteDataSource: ProviderRemoteDataSource,
        providerLocalDataSource: ProviderLocalDataSource,
        providerMapper: ProviderMapper
    ): ProviderRepository {
        return ProviderRepositoryImpl(providerRemoteDataSource, providerLocalDataSource, providerMapper)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        paymentRemoteDataSource: PaymentRemoteDataSource,
        paymentLocalDataSource: PaymentLocalDataSource,
        paymentMapper: PaymentMapper
    ): PaymentRepository {
        return PaymentRepositoryImpl(paymentRemoteDataSource, paymentLocalDataSource, paymentMapper)
    }

    @Provides
    @Singleton
    fun provideReviewRepository(
        reviewRemoteDataSource: ReviewRemoteDataSource,
        reviewLocalDataSource: ReviewLocalDataSource,
        reviewMapper: ReviewMapper
    ): ReviewRepository {
        return ReviewRepositoryImpl(reviewRemoteDataSource, reviewLocalDataSource, reviewMapper)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        messageRemoteDataSource: MessageRemoteDataSource,
        messageLocalDataSource: MessageLocalDataSource,
        messageMapper: MessageMapper
    ): MessageRepository {
        return MessageRepositoryImpl(messageRemoteDataSource, messageLocalDataSource, messageMapper)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatRemoteDataSource: ChatRemoteDataSource,
        chatLocalDataSource: ChatLocalDataSource,
        chatMapper: ChatMapper
    ): ChatRepository {
        return ChatRepositoryImpl(chatRemoteDataSource, chatLocalDataSource, chatMapper)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryRemoteDataSource: CategoryRemoteDataSource,
        categoryLocalDataSource: CategoryLocalDataSource,
        categoryMapper: CategoryMapper
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryRemoteDataSource, categoryLocalDataSource, categoryMapper)
    }

    @Provides
    @Singleton
    fun provideAssistantListRepository(
        assistantListRemoteDataSource: AssistantListRemoteDataSource,
        assistantListLocalDataSource: AssistantListLocalDataSource,
        assistantListMapper: AssistantListMapper
    ): AssistantListRepository {
        return AssistantListRepositoryImpl(assistantListRemoteDataSource, assistantListLocalDataSource, assistantListMapper)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationRemoteDataSource: NotificationRemoteDataSource,
        notificationLocalDataSource: NotificationLocalDataSource,
        notificationMapper: NotificationMapper
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationRemoteDataSource, notificationLocalDataSource, notificationMapper)
    }

    @Provides
    @Singleton
    fun provideServiceAvailabilityRepository(
        serviceAvailabilityRemoteDataSource: ServiceAvailabilityRemoteDataSource,
        serviceAvailabilityLocalDataSource: ServiceAvailabilityLocalDataSource,
        serviceAvailabilityMapper: ServiceAvailabilityMapper
    ): ServiceAvailabilityRepository {
        return ServiceAvailabilityRepositoryImpl(serviceAvailabilityRemoteDataSource, serviceAvailabilityLocalDataSource, serviceAvailabilityMapper)
    }

}
