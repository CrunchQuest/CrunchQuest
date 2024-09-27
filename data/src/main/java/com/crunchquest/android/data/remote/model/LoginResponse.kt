package com.crunchquest.android.data.remote.model

import com.crunchquest.android.data.model.remote.UserRemote
import com.google.gson.annotations.SerializedName

data class  LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserRemote
)