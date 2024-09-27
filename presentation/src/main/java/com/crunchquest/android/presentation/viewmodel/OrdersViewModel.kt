
package com.crunchquest.android.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crunchquest.android.domain.entities.Request
import com.crunchquest.android.domain.model.Order
import com.crunchquest.android.domain.repository.RequestRepository
import com.crunchquest.android.domain.repository.UserRepository

class OrdersViewModel(private val repository: RequestRepository) : ViewModel() {
    private val _requests = MutableLiveData<List<Order>>()
    val requests: LiveData<List<Order>> get() = _requests

    private val _assists = MutableLiveData<List<Order>>()
    val assists: LiveData<List<Order>> get() = _assists

}
