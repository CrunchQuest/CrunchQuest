package com.crunchquest.android.presentation.di

import android.content.Context
import androidx.room.Room
import com.crunchquest.android.data.remote.api.ApiService
import com.crunchquest.android.data.source.local.AppDatabase
import com.crunchquest.android.data.source.local.AssistantDao
import com.crunchquest.android.data.source.local.AssistantListDao
import com.crunchquest.android.data.source.local.AssistantListLocalDataSource
import com.crunchquest.android.data.source.local.AssistantLocalDataSource
import com.crunchquest.android.data.source.local.BookingDao
import com.crunchquest.android.data.source.local.BookingLocalDataSource
import com.crunchquest.android.data.source.local.CategoryDao
import com.crunchquest.android.data.source.local.CategoryLocalDataSource
import com.crunchquest.android.data.source.local.ChatDao
import com.crunchquest.android.data.source.local.ChatLocalDataSource
import com.crunchquest.android.data.source.local.MessageDao
import com.crunchquest.android.data.source.local.MessageLocalDataSource
import com.crunchquest.android.data.source.local.NotificationDao
import com.crunchquest.android.data.source.local.NotificationLocalDataSource
import com.crunchquest.android.data.source.local.PaymentDao
import com.crunchquest.android.data.source.local.PaymentLocalDataSource
import com.crunchquest.android.data.source.local.ProviderDao
import com.crunchquest.android.data.source.local.ProviderLocalDataSource
import com.crunchquest.android.data.source.local.RequestDao
import com.crunchquest.android.data.source.local.RequestLocalDataSource
import com.crunchquest.android.data.source.local.ReviewDao
import com.crunchquest.android.data.source.local.ReviewLocalDataSource
import com.crunchquest.android.data.source.local.ServiceAvailabilityDao
import com.crunchquest.android.data.source.local.ServiceAvailabilityLocalDataSource
import com.crunchquest.android.data.source.local.ServiceDao
import com.crunchquest.android.data.source.local.ServiceLocalDataSource
import com.crunchquest.android.data.source.local.UserDao
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "cq_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    // Provide RequestDao
    @Provides
    @Singleton
    fun provideRequestDao(appDatabase: AppDatabase): RequestDao {
        return appDatabase.requestDao()
    }

    // Provide ProviderDao
    @Provides
    @Singleton
    fun provideProviderDao(appDatabase: AppDatabase): ProviderDao {
        return appDatabase.providerDao()
    }

    // Provide BookingDao
    @Provides
    @Singleton
    fun provideBookingDao(appDatabase: AppDatabase): BookingDao {
        return appDatabase.bookingDao()
    }

    // Other DAOs
    @Provides
    @Singleton
    fun provideServiceDao(appDatabase: AppDatabase): ServiceDao {
        return appDatabase.serviceDao()
    }

    @Provides
    @Singleton
    fun provideAssistantDao(appDatabase: AppDatabase): AssistantDao {
        return appDatabase.assistantDao()
    }

    @Provides
    @Singleton
    fun provideAssistantListDao(appDatabase: AppDatabase): AssistantListDao {
        return appDatabase.assistantListDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @Provides
    @Singleton
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(appDatabase: AppDatabase): NotificationDao {
        return appDatabase.notificationDao()
    }

    @Provides
    @Singleton
    fun providePaymentDao(appDatabase: AppDatabase): PaymentDao {
        return appDatabase.paymentDao()
    }

    @Provides
    @Singleton
    fun provideReviewDao(appDatabase: AppDatabase): ReviewDao {
        return appDatabase.reviewDao()
    }

    @Provides
    @Singleton
    fun provideServiceAvailabilityDao(appDatabase: AppDatabase): ServiceAvailabilityDao {
        return appDatabase.serviceAvailabilityDao()
    }

    // Provide Firebase Firestore (or other Remote dependencies)
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // Provide Remote Data Sources
    @Provides
    @Singleton
    fun provideUserRemoteDataSource(apiService: ApiService): UserRemoteDataSource {
        return UserRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideRequestRemoteDataSource(apiService: ApiService): RequestRemoteDataSource {
        return RequestRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideServiceRemoteDataSource(firestore: FirebaseFirestore): ServiceRemoteDataSource {
        return ServiceRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideBookingRemoteDataSource(apiService: ApiService): BookingRemoteDataSource {
        return BookingRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideAssistantRemoteDataSource(apiService: ApiService): AssistantRemoteDataSource {
        return AssistantRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideAssistantListRemoteDataSource(apiService: ApiService): AssistantListRemoteDataSource {
        return AssistantListRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideCategoryRemoteDataSource(firestore: FirebaseFirestore): CategoryRemoteDataSource {
        return CategoryRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideChatRemoteDataSource(firestore: FirebaseFirestore): ChatRemoteDataSource {
        return ChatRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideMessageRemoteDataSource(firestore: FirebaseFirestore): MessageRemoteDataSource {
        return MessageRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideNotificationRemoteDataSource(firestore: FirebaseFirestore): NotificationRemoteDataSource {
        return NotificationRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun providePaymentRemoteDataSource(firestore: FirebaseFirestore): PaymentRemoteDataSource {
        return PaymentRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideReviewRemoteDataSource(firestore: FirebaseFirestore): ReviewRemoteDataSource {
        return ReviewRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideServiceAvailabilityRemoteDataSource(firestore: FirebaseFirestore): ServiceAvailabilityRemoteDataSource {
        return ServiceAvailabilityRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideProviderRemoteDataSource(firestore: FirebaseFirestore): ProviderRemoteDataSource {
        return ProviderRemoteDataSource(firestore)
    }

    // Provide Local Data Sources using DAOs from AppDatabase
    @Provides
    @Singleton
    fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource {
        return UserLocalDataSource(userDao)
    }

    @Provides
    @Singleton
    fun provideRequestLocalDataSource(requestDao: RequestDao): RequestLocalDataSource {
        return RequestLocalDataSource(requestDao)
    }

    @Provides
    @Singleton
    fun provideServiceLocalDataSource(serviceDao: ServiceDao): ServiceLocalDataSource {
        return ServiceLocalDataSource(serviceDao)
    }

    @Provides
    @Singleton
    fun provideAssistantListLocalDataSource(assistantListDao: AssistantListDao): AssistantListLocalDataSource {
        return AssistantListLocalDataSource(assistantListDao)
    }

    @Provides
    @Singleton
    fun provideAssistantLocalDataSource(assistantDao: AssistantDao): AssistantLocalDataSource {
        return AssistantLocalDataSource(assistantDao)
    }

    @Provides
    @Singleton
    fun provideBookingLocalDataSource(bookingDao: BookingDao): BookingLocalDataSource {
        return BookingLocalDataSource(bookingDao)
    }

    @Provides
    @Singleton
    fun provideCategoryLocalDataSource(categoryDao: CategoryDao): CategoryLocalDataSource {
        return CategoryLocalDataSource(categoryDao)
    }

    @Provides
    @Singleton
    fun provideChatLocalDataSource(chatDao: ChatDao): ChatLocalDataSource {
        return ChatLocalDataSource(chatDao)
    }

    @Provides
    @Singleton
    fun provideMessageLocalDataSource(messageDao: MessageDao): MessageLocalDataSource {
        return MessageLocalDataSource(messageDao)
    }

    @Provides
    @Singleton
    fun provideNotificationLocalDataSource(notificationDao: NotificationDao): NotificationLocalDataSource {
        return NotificationLocalDataSource(notificationDao)
    }

    @Provides
    @Singleton
    fun providePaymentLocalDataSource(paymentDao: PaymentDao): PaymentLocalDataSource {
        return PaymentLocalDataSource(paymentDao)
    }

    @Provides
    @Singleton
    fun provideProviderLocalDataSource(providerDao: ProviderDao): ProviderLocalDataSource {
        return ProviderLocalDataSource(providerDao)
    }

    @Provides
    @Singleton
    fun provideReviewLocalDataSource(reviewDao: ReviewDao): ReviewLocalDataSource {
        return ReviewLocalDataSource(reviewDao)
    }

    @Provides
    @Singleton
    fun provideServiceAvailabilityLocalDataSource(serviceAvailabilityDao: ServiceAvailabilityDao): ServiceAvailabilityLocalDataSource {
        return ServiceAvailabilityLocalDataSource(serviceAvailabilityDao)
    }

}
