package com.crunchquest.android.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserSellerInfo(
    var uid: String? = "",
    var description: String? = "",
    var previousSchool: String? = "",
    var educationalAttainment: String = "",
    var totalRating: Int? = 0,
    var count: Int? = 0,
    var totalJobsFinished: Int? = 0
)