package com.example.crunchquest.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crunchquest.data.QuestRepository
import com.example.crunchquest.data.model.QuestList
import com.example.crunchquest.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: QuestRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<QuestList>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<QuestList>>>
        get() = _uiState

    fun getAllData() {
        viewModelScope.launch {
            repository.getAllData()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { questList ->
                    _uiState.value = UiState.Success(questList)
                }
        }
    }

}