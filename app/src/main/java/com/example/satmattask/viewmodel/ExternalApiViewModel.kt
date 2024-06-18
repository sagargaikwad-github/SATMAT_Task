package com.example.satmattask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satmattask.model.zaakPay.UpiIntentModel
import com.example.satmattask.repository.ExternalApiRespository
import com.example.satmattask.repository.ResponseSealed
import com.example.satmattask.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExternalApiViewModel(private val apiRespository: ExternalApiRespository) : ViewModel() {
    private val _getUpiIntentResponse = MutableLiveData<ResponseSealed<UpiIntentModel>>()
    val getUpiIntentResponse: LiveData<ResponseSealed<UpiIntentModel>>
        get() = _getUpiIntentResponse

    fun getUpiIntentResult(
        data: String,
        checkSUM: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _getUpiIntentResponse.postValue(apiRespository.getUpiIntent(data, checkSUM))
        }
    }
}