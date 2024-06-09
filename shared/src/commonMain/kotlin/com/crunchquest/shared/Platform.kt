package com.crunchquest.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform