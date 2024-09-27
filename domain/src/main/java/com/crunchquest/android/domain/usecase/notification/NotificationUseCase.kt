package com.crunchquest.android.domain.usecase.notification

import com.crunchquest.android.domain.entities.Notification
import com.crunchquest.android.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Notification>> {
        return notificationRepository.getNotification(userId)
    }
}
