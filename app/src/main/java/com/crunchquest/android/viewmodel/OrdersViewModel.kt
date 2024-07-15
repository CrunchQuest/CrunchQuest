
package com.crunchquest.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crunchquest.android.data.model.Order
import com.crunchquest.android.repository.UserRepository

class OrdersViewModel(private val repository: UserRepository) : ViewModel() {
    private val _requests = MutableLiveData<List<Order>>()
    val requests: LiveData<List<Order>> get() = _requests

    private val _assists = MutableLiveData<List<Order>>()
    val assists: LiveData<List<Order>> get() = _assists

    fun fetchOrdersRequest(userId: String) {
        repository.fetchOrdersByUser(userId, "NEW") { orders ->
            _requests.postValue(orders)
        }
    }

    fun fetchOrdersAssist(userId: String) {
        repository.fetchOrdersToUser(userId, "NEW") { orders ->
            _assists.postValue(orders)
        }
    }
}
