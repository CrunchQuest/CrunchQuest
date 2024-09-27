package com.crunchquest.android.presentation.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crunchquest.android.domain.entities.Assistant
import com.crunchquest.android.domain.entities.Booking
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.domain.usecase.assistant.GetAssistantsByRequestUseCase
import com.crunchquest.android.domain.usecase.assistant.GetAssistantsByStatusUseCase
import com.crunchquest.android.domain.usecase.booking.GetBookingUseCase
import com.crunchquest.android.domain.usecase.booking.GetBookingsByStatusUseCase
import com.crunchquest.android.domain.usecase.request.GetRequestUseCase
import com.crunchquest.android.domain.usecase.request.GetRequestsByStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getRequestsByStatusUseCase: GetRequestsByStatusUseCase,
    private val getAssistantsByStatusUseCase: GetAssistantsByStatusUseCase,
    private val getBookingsByStatusUseCase: GetBookingsByStatusUseCase
) : ViewModel() {

    private val _filteredActivities = MutableLiveData<Result<List<ActivityItem>>>()
    val filteredActivities: LiveData<Result<List<ActivityItem>>> = _filteredActivities

    var currentType: ActivityType = ActivityType.REQUEST
        private set
    private var currentStatus: String = "All"

    init {
        fetchFilteredData()
    }

    fun setTypeFilter(type: ActivityType) {
        currentType = type
        fetchFilteredData()
    }

    fun setStatusFilter(status: String) {
        currentStatus = status
        fetchFilteredData()
    }

    private fun fetchFilteredData() {
        viewModelScope.launch {
            _filteredActivities.value = Result.Loading
            val result: Result<List<ActivityItem>> = when (currentType) {
                ActivityType.REQUEST -> {
                    if (currentStatus == "All") {
                        // Fetch all requests without filtering by status
                        getRequestsByStatusUseCase("All").mapRequestToActivityItems()
                    } else {
                        // Fetch requests filtered by the current status
                        getRequestsByStatusUseCase(currentStatus).mapRequestToActivityItems()
                    }
                }
                ActivityType.ASSIST -> {
                    if (currentStatus == "All") {
                        getAssistantsByStatusUseCase("All").mapAssistToActivityItems()
                    } else {
                        getAssistantsByStatusUseCase(currentStatus).mapAssistToActivityItems()
                    }
                }
                ActivityType.BOOKING -> {
                    if (currentStatus == "All") {
                        getBookingsByStatusUseCase("All").mapBookingToActivityItems()
                    } else {
                        getBookingsByStatusUseCase(currentStatus).mapBookingToActivityItems()
                    }
                }
            }
            _filteredActivities.value = result
        }
    }

    // Extension functions for mapping results to ActivityItem
    private fun Result<List<Request>>.mapRequestToActivityItems(): Result<List<ActivityItem>> {
        return when (this) {
            is Result.Success -> Result.Success(data.map { ActivityItem.RequestItem(it) })
            is Result.Error -> Result.Error(exception)
            Result.Loading -> Result.Loading
        }
    }

    private fun Result<List<Assistant>>.mapAssistToActivityItems(): Result<List<ActivityItem>> {
        return when (this) {
            is Result.Success -> Result.Success(data.map { ActivityItem.AssistItem(it) })
            is Result.Error -> Result.Error(exception)
            Result.Loading -> Result.Loading
        }
    }

    private fun Result<List<Booking>>.mapBookingToActivityItems(): Result<List<ActivityItem>> {
        return when (this) {
            is Result.Success -> Result.Success(data.map { ActivityItem.BookingItem(it) })
            is Result.Error -> Result.Error(exception)
            Result.Loading -> Result.Loading
        }
    }
}

enum class ActivityType {
    REQUEST,
    ASSIST,
    BOOKING
}
