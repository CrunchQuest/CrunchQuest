package com.crunchquest.android.presentation.ui.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.usecase.request.CreateRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.crunchquest.android.domain.utility.Result
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateRequestViewModel @Inject constructor(
    private val createRequestUseCase: CreateRequestUseCase
) : ViewModel() {

    private val _createRequestResult = MutableLiveData<Result<Request>>()
    val createRequestResult: LiveData<Result<Request>> = _createRequestResult

    fun createRequest(request: Request) {
        viewModelScope.launch {
            _createRequestResult.value = Result.Loading
            val result = createRequestUseCase(request)
            _createRequestResult.value = result
        }
    }
}

