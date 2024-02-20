package com.example.crunchquest.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class RequestMessage (
    var uid: String? = "",
    var fromId: String? = "",
    var toId: String? = "",
    var text: String? = "",
    var timeStamp: Long? = 0,
    var requestUid: String? = ""
)