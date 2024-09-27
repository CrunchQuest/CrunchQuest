package com.crunchquest.android.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.crunchquest.android.domain.utility.Result
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> get() = _profileState

    fun fetchUserProfile(userId: String) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading

            val result = getUserUseCase(userId)
            _profileState.value = when (result) {
                is Result.Success -> ProfileState.Success(result.data)
                is Result.Error -> ProfileState.Error(result.exception.message ?: "Unknown error")
                Result.Loading -> ProfileState.Loading
            }
        }
    }
}

