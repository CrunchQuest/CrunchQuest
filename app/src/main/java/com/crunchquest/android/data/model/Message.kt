package com.crunchquest.android.data.model

class Message(
    var uid: String? = "",
    var fromId: String? = "",
    var toId: String? = "",
    var text: String? = "",
    var timeStamp: Long? = 0,
    var senderName: String? = ""
)