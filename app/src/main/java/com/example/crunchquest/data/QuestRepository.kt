package com.example.crunchquest.data

import com.example.crunchquest.data.model.QuestData.quests
import com.example.crunchquest.data.model.QuestList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class QuestRepository {
    private val quest = mutableListOf<QuestList>()

    init {
        if (quest.isEmpty()) {
            quests.forEach {
                quest.add(QuestList(it, 0))
            }
        }
    }

    fun getAllData(): Flow<List<QuestList>> {
        return flowOf(quest)
    }

    fun getById(id: Long): QuestList {
        return quest.first {
            it.quest.id == id
        }
    }

    companion object {
        @Volatile
        private var instance: QuestRepository? = null

        fun getInstance(): QuestRepository =
            instance ?: synchronized(this) {
                QuestRepository().apply {
                    instance = this
                }
            }
    }
}