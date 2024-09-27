package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.ServiceAvailabilityRemote
import com.crunchquest.android.data.source.local.ServiceAvailabilityLocal
import com.crunchquest.android.domain.entities.ServiceAvailability
import javax.inject.Inject

class ServiceAvailabilityMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(availabilityRemote: ServiceAvailabilityRemote): ServiceAvailability {
        return ServiceAvailability(
            availabilityId = availabilityRemote.availabilityId,
            serviceId = availabilityRemote.serviceId,
            dayOfWeek = availabilityRemote.dayOfWeek,
            startTime = availabilityRemote.startTime,
            endTime = availabilityRemote.endTime,
            status = availabilityRemote.status,
            createdAt = availabilityRemote.createdAt,
            updatedAt = availabilityRemote.updatedAt
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(availability: ServiceAvailability): ServiceAvailabilityRemote {
        return ServiceAvailabilityRemote(
            availabilityId = availability.availabilityId,
            serviceId = availability.serviceId,
            dayOfWeek = availability.dayOfWeek,
            startTime = availability.startTime,
            endTime = availability.endTime,
            status = availability.status,
            createdAt = availability.createdAt,
            updatedAt = availability.updatedAt
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(availabilityLocal: ServiceAvailabilityLocal): ServiceAvailability {
        return ServiceAvailability(
            availabilityId = availabilityLocal.availabilityId,
            serviceId = availabilityLocal.serviceId,
            dayOfWeek = availabilityLocal.dayOfWeek,
            startTime = availabilityLocal.startTime,
            endTime = availabilityLocal.endTime,
            status = availabilityLocal.status,
            createdAt = availabilityLocal.createdAt,
            updatedAt = availabilityLocal.updatedAt
        )
    }

    // Convert domain entity to local data model
    fun toLocal(availability: ServiceAvailability): ServiceAvailabilityLocal {
        return ServiceAvailabilityLocal(
            availabilityId = availability.availabilityId,
            serviceId = availability.serviceId,
            dayOfWeek = availability.dayOfWeek,
            startTime = availability.startTime,
            endTime = availability.endTime,
            status = availability.status,
            createdAt = availability.createdAt,
            updatedAt = availability.updatedAt
        )
    }
}
