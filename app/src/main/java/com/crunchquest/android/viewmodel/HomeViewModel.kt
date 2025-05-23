package com.crunchquest.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.data.model.ServiceRequest
import com.crunchquest.android.data.network.response.ServiceRequestResponse
import com.crunchquest.android.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    private val _serviceRequests = MutableLiveData<List<ServiceRequest>>()
    val serviceRequests: LiveData<List<ServiceRequest>> get() = _serviceRequests

    private val _userProfileImageUrl = MutableLiveData<String>()
    val userProfileImageUrl: LiveData<String> get() = _userProfileImageUrl

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchServiceRequests(userId: String) {
        viewModelScope.launch {
            repository.getServiceRequests(userId).onSuccess { serviceRequests ->
                val serviceRequestsList = serviceRequests.map { convertToServiceRequest(it) }
                _serviceRequests.postValue(serviceRequestsList)
                _error.postValue(null)  // Clear previous error
            }.onFailure { exception ->
                Log.e("HomeViewModel", "Error fetching service requests: ${exception.message}")
                _error.postValue("Failed to fetch service requests. Please try again later.")
            }
        }
    }

    fun fetchUserProfileImage(userId: String) {
        repository.fetchUserProfileImage(userId) { imageUrl ->
            _userProfileImageUrl.postValue(imageUrl)
        }
    }

    private fun convertToServiceRequest(serviceRequestResponse: ServiceRequestResponse): ServiceRequest {
        return ServiceRequest(
            uid = serviceRequestResponse.uid,
            address = serviceRequestResponse.request_data.address,
            assistConfirmation = serviceRequestResponse.request_data.assistConfirmation,
            bookedBy = serviceRequestResponse.request_data.bookedBy,
            category = serviceRequestResponse.request_data.category,
            categoryId = serviceRequestResponse.request_data.categoryId,
            date = serviceRequestResponse.request_data.date,
            description = serviceRequestResponse.request_data.description,
            latitude = serviceRequestResponse.request_data.latitude ?: 0.0,
            longitude = serviceRequestResponse.request_data.longitude ?: 0.0,
            modeOfPayment = serviceRequestResponse.request_data.modeOfPayment,
            price = serviceRequestResponse.request_data.price ?: 0,
            time = serviceRequestResponse.request_data.time,
            title = serviceRequestResponse.request_data.title,
            userUid = serviceRequestResponse.request_data.userUid,
            reviewed = serviceRequestResponse.request_data.reviewed
        )
    }
}