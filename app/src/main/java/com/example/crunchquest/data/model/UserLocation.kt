package com.example.crunchquest.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserLocation(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var timestamp: Long = 0
)