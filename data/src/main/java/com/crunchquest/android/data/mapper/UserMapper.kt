package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.UserRemote
import com.crunchquest.android.data.source.local.UserLocal
import com.crunchquest.android.domain.entities.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    // Mapping from remote data model to domain entity
    fun fromRemote(userRemote: UserRemote): User {
        return User(
            userId = userRemote.userId,
            email = userRemote.email,
            firstName = userRemote.firstName,
            lastName = userRemote.lastName,
            bio = userRemote.bio,
            avgRating = userRemote.avgRating,
            dateOfBirth = userRemote.dateOfBirth,
            gender = userRemote.gender,
            phoneNumber = userRemote.phoneNumber,
            profilePicture = userRemote.profilePicture,
            idCard = userRemote.idCard,
            preference = userRemote.preference,
            createdAt = userRemote.createdAt,
            updatedAt = userRemote.updatedAt,
            lastLogin = userRemote.lastLogin,
            status = userRemote.status,
            role = userRemote.role,
            verificationStatus = userRemote.verificationStatus,
            workExperience = userRemote.workExperience
        )
    }

    // Mapping from domain entity to remote data model
    fun toRemote(user: User): UserRemote {
        return UserRemote(
            userId = user.userId,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            bio = user.bio,
            avgRating = user.avgRating,
            dateOfBirth = user.dateOfBirth,
            gender = user.gender,
            phoneNumber = user.phoneNumber,
            profilePicture = user.profilePicture,
            idCard = user.idCard,
            preference = user.preference,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            lastLogin = user.lastLogin,
            status = user.status,
            role = user.role,
            verificationStatus = user.verificationStatus,
            workExperience = user.workExperience
        )
    }

    // Remove password from toLocal() method
    fun toLocal(user: User): UserLocal {
        return UserLocal(
            userId = user.userId,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            bio = user.bio,
            avgRating = user.avgRating,
            dateOfBirth = user.dateOfBirth,
            gender = user.gender,
            phoneNumber = user.phoneNumber,
            profilePicture = user.profilePicture,
            idCard = user.idCard,
            preference = user.preference,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            lastLogin = user.lastLogin,
            status = user.status,
            role = user.role,
            verificationStatus = user.verificationStatus,
            workExperience = user.workExperience
        )
    }

    // Remove password from fromLocal() method
    fun fromLocal(userLocal: UserLocal): User {
        return User(
            userId = userLocal.userId,
            email = userLocal.email,
            firstName = userLocal.firstName,
            lastName = userLocal.lastName,
            bio = userLocal.bio,
            avgRating = userLocal.avgRating,
            dateOfBirth = userLocal.dateOfBirth,
            gender = userLocal.gender,
            phoneNumber = userLocal.phoneNumber,
            profilePicture = userLocal.profilePicture,
            idCard = userLocal.idCard,
            preference = userLocal.preference,
            createdAt = userLocal.createdAt,
            updatedAt = userLocal.updatedAt,
            lastLogin = userLocal.lastLogin,
            status = userLocal.status,
            role = userLocal.role,
            verificationStatus = userLocal.verificationStatus,
            workExperience = userLocal.workExperience
        )
    }
}
