package com.example.crunchquest.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crunchquest.data.QuestRepository
import com.example.crunchquest.data.model.QuestList
import com.example.crunchquest.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: QuestRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<QuestList>> = MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<QuestList>>
        get() = _uiState

    fun getById(id: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getById(id))
        }
    }
}