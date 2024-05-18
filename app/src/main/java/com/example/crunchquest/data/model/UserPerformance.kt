package com.example.crunchquest.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserPerformance(
    var rating: Int = 0,
    var total: Int = 0,
    var category_name: String = "",
)