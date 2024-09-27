package com.crunchquest.android.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2
import com.crunchquest.android.data.utility.Converters

@Database(entities = [
    AssistantLocal::class,
    AssistantListLocal::class,
    UserLocal::class,
    ServiceLocal::class,
    RequestLocal::class,
    ProviderLocal::class,
    BookingLocal::class,
    ReviewLocal::class,
    NotificationLocal::class,
    ChatLocal::class,
    MessageLocal::class,
    PaymentLocal::class,
    ServiceAvailabilityLocal::class,
    CategoryLocal::class, ], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun serviceDao(): ServiceDao
    abstract fun requestDao(): RequestDao
    abstract fun assistantDao(): AssistantDao
    abstract fun assistantListDao(): AssistantListDao
    abstract fun providerDao(): ProviderDao
    abstract fun bookingDao(): BookingDao
    abstract fun reviewDao(): ReviewDao
    abstract fun notificationDao(): NotificationDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun paymentDao(): PaymentDao
    abstract fun serviceAvailabilityDao(): ServiceAvailabilityDao
    abstract fun categoryDao(): CategoryDao
    // Add other DAOs here

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cq_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

