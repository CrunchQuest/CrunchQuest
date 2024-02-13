package com.example.crunchquest.di

import com.example.crunchquest.data.QuestRepository

object Injection {
    fun provideRepository(): QuestRepository {
        return QuestRepository.getInstance()
    }
}