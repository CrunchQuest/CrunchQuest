package com.crunchquest.android.presentation.ui.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.entities.AssistantList
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.domain.usecase.assistant.GetAssistantListByRequestUseCase
import com.crunchquest.android.domain.usecase.request.CancelRequestUseCase
import com.crunchquest.android.domain.usecase.request.GetRequestDetailsUseCase
import com.crunchquest.android.domain.usecase.user.GetUserByIdUseCase
import com.crunchquest.android.domain.utility.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    private val getRequestDetailUseCase: GetRequestDetailsUseCase,
    private val getAssistantListByRequestUseCase: GetAssistantListByRequestUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val cancelRequestUseCase: CancelRequestUseCase // Add a new use case for canceling requests
) : ViewModel() {

    private val _requestDetail = MutableLiveData<Result<Request>>()
    val requestDetail: LiveData<Result<Request>> get() = _requestDetail

    private val _assistantList = MutableLiveData<Result<AssistantList>>()
    val assistantList: LiveData<Result<AssistantList>> get() = _assistantList

    fun fetchRequestDetail(requestId: String) {
        viewModelScope.launch {
            _requestDetail.value = Result.Loading
            val result = getRequestDetailUseCase(requestId)
            _requestDetail.value = result
            fetchAssistantList(requestId) // Call this to fetch assistants after fetching request details
        }
    }

    fun fetchAssistantList(requestId: String) {
        viewModelScope.launch {
            _assistantList.value = Result.Loading
            val result = getAssistantListByRequestUseCase(requestId)
            _assistantList.value = result
        }
    }

    suspend fun getUserById(userId: String): Result<User> {
        return getUserByIdUseCase(userId)
    }

    fun confirmAssistance(request: Request) {
        viewModelScope.launch {
            try {
                fetchAssistantList(request.requestId)
            } catch (e: Exception) {
                Log.e("RequestDetailViewModel", "Error confirming assistance", e)
            }
        }
    }

    fun cancelRequest(requestId: String) {
        viewModelScope.launch {
            try {
                val result = cancelRequestUseCase(requestId)
                if (result is Result.Success) {
                    // Handle successful cancellation, update UI or notify user
                    Log.d("RequestDetailViewModel", "Request successfully canceled.")
                } else if (result is Result.Error) {
                    // Handle the error, update UI state, show error message
                    Log.e("RequestDetailViewModel", "Failed to cancel request: ${result.exception.message}")
                }
            } catch (e: Exception) {
                Log.e("RequestDetailViewModel", "Error canceling request", e)
            }
        }
    }
}

