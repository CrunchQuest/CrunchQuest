package com.example.crunchquest.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserReview(
    var uid: String? = "",
    var userUid: String? = "",
    var review: String = "",
    var rating: Int? = 0,
    var categoryId: Int? = 0
)