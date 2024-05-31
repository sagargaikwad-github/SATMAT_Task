package com.example.satmattask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satmattask.model.RechargeModel
import com.example.satmattask.repository.ServiceRepository
import com.example.satmattask.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceViewModel(private val serviceRepository: ServiceRepository) : ViewModel() {
    val rechargeResponse: LiveData<Response<RechargeModel>>
        get() = serviceRepository.serviceLiveData

    fun doRecharge(
        memberId: String,
        apiPassword: String,
        apiPin: String,
        number: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.doRechargeRepository(memberId, apiPassword, apiPin, number)
        }
    }
}