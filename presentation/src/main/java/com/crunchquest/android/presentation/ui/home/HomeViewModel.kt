package com.crunchquest.android.presentation.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.entities.Provider
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.usecase.booking.GetBookingUseCase
import com.crunchquest.android.domain.usecase.provider.GetProviderUseCase
import com.crunchquest.android.domain.usecase.request.GetFeaturedRequestsUseCase
import com.crunchquest.android.domain.usecase.request.GetRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeaturedRequestsUseCase: GetFeaturedRequestsUseCase,
    private val getRequestUseCase: GetRequestUseCase,
    private val getProviderUseCase: GetProviderUseCase,
    private val getBookingUseCase: GetBookingUseCase
) : ViewModel() {

    private val _featuredRequests = MutableLiveData<Result<List<Request>>>()
    val featuredRequests: LiveData<Result<List<Request>>> = _featuredRequests

    private val _serviceHeroes = MutableLiveData<Result<List<Provider>>>()
    val serviceHeroes: LiveData<Result<List<Provider>>> = _serviceHeroes

    private val _recentActivities = MutableLiveData<Result<List<RecentActivityItem>>>()
    val recentActivities: LiveData<Result<List<RecentActivityItem>>> = _recentActivities

    init {
        loadFeaturedRequests()
        loadServiceHeroes()
        loadRecentActivities()
    }

    // Function to load featured requests using GetFeaturedRequestsUseCase
    fun loadFeaturedRequests() {
        viewModelScope.launch {
            _featuredRequests.value = Result.Loading
            try {
                // Fetching featured requests from the backend
                val result = getRequestUseCase.invoke("feature") // Using the dedicated use case for featured requests
                _featuredRequests.value = result
            } catch (e: Exception) {
                _featuredRequests.value = Result.Error(e)
                Log.e("HomeViewModel", "Error loading featured requests", e)
            }
        }
    }

    // Function to load service heroes (providers)
    fun loadServiceHeroes() {
        viewModelScope.launch {
            _serviceHeroes.value = Result.Loading
            try {
                val result = getProviderUseCase.invoke("service_heroes") // Assuming "service_heroes" filters the appropriate providers
                _serviceHeroes.value = result
            } catch (e: Exception) {
                _serviceHeroes.value = Result.Error(e)
            }
        }
    }

    // Function to load recent activities by combining recent requests and bookings
    fun loadRecentActivities() {
        viewModelScope.launch {
            _recentActivities.value = Result.Loading
            try {
                // Fetch recent requests and bookings for recent activity
                val recentRequests = getRequestUseCase.invoke("recent")
                val recentBookings = getBookingUseCase.invoke("recent")

                // Combine and sort them into one list based on timestamp
                val combinedActivities = mutableListOf<RecentActivityItem>()

                if (recentRequests is Result.Success) {
                    combinedActivities.addAll(recentRequests.data.map { RecentActivityItem.RequestItem(it) })
                }

                if (recentBookings is Result.Success) {
                    combinedActivities.addAll(recentBookings.data.map { RecentActivityItem.BookingItem(it) })
                }

                // Sorting by recent timestamp
                combinedActivities.sortByDescending { it.getTimestamp() }
                _recentActivities.value = Result.Success(combinedActivities)
            } catch (e: Exception) {
                _recentActivities.value = Result.Error(e)
            }
        }
    }
}


