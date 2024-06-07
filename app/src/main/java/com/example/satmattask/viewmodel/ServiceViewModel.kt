package com.example.satmattask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satmattask.model.RechargeModel
import com.example.satmattask.model.getOperators.GetOperators
import com.example.satmattask.model.getRechargePlans.RechargePlans
import com.example.satmattask.repository.ServiceRepository
import com.example.satmattask.repository.ResponseSealed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceViewModel(private val serviceRepository: ServiceRepository) : ViewModel() {

    private val _responseData = MutableLiveData<ResponseSealed<GetOperators>>()
    private val _rechargePlansData = MutableLiveData<ResponseSealed<RechargePlans>>()
    val rechargeResponse: LiveData<ResponseSealed<RechargeModel>>
        get() = serviceRepository.serviceLiveData

    val getOperatorResponse: LiveData<ResponseSealed<GetOperators>>
        get() = _responseData

    val getRechargePlansResponse : LiveData<ResponseSealed<RechargePlans>>
        get() = _rechargePlansData

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

    fun getOperators(service : String) {
        viewModelScope.launch(Dispatchers.IO) {
            _responseData.postValue(serviceRepository.getOperators(service))
        }
    }

    fun getRechargePlans(operator: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _rechargePlansData.postValue(serviceRepository.getRechargePlans(operator, phone))
        }
    }

}