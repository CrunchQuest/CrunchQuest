package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.ServiceRemote
import com.crunchquest.android.data.source.local.ServiceLocal
import com.crunchquest.android.domain.entities.Service
import javax.inject.Inject

class ServiceMapper @Inject constructor() {

    // Mapping from remote data model to domain entity
    fun fromRemote(serviceRemote: ServiceRemote): Service {
        return Service(
            serviceId = serviceRemote.serviceId,
            providerId = serviceRemote.providerId,
            serviceName = serviceRemote.serviceName,
            serviceDescription = serviceRemote.serviceDescription,
            price = serviceRemote.price,
            category = serviceRemote.category,
            status = serviceRemote.status,
            serviceImage = serviceRemote.serviceImage,
            createdAt = serviceRemote.createdAt,
            updatedAt = serviceRemote.updatedAt
        )
    }

    // Mapping from domain entity to remote data model
    fun toRemote(service: Service): ServiceRemote {
        return ServiceRemote(
            serviceId = service.serviceId,
            providerId = service.providerId,
            serviceName = service.serviceName,
            serviceDescription = service.serviceDescription,
            price = service.price,
            category = service.category,
            status = service.status,
            serviceImage = service.serviceImage,
            createdAt = service.createdAt,
            updatedAt = service.updatedAt
        )
    }

    // Mapping from domain entity to local data model
    fun toLocal(service: Service): ServiceLocal {
        return ServiceLocal(
            serviceId = service.serviceId,
            providerId = service.providerId,
            serviceName = service.serviceName,
            serviceDescription = service.serviceDescription,
            price = service.price,
            category = service.category,
            status = service.status,
            serviceImage = service.serviceImage,
            createdAt = service.createdAt,
            updatedAt = service.updatedAt
        )
    }

    // Mapping from local data model to domain entity
    fun fromLocal(serviceLocal: ServiceLocal): Service {
        return Service(
            serviceId = serviceLocal.serviceId,
            providerId = serviceLocal.providerId,
            serviceName = serviceLocal.serviceName,
            serviceDescription = serviceLocal.serviceDescription,
            price = serviceLocal.price,
            category = serviceLocal.category,
            status = serviceLocal.status,
            serviceImage = serviceLocal.serviceImage,
            createdAt = serviceLocal.createdAt,
            updatedAt = serviceLocal.updatedAt
        )
    }
}
