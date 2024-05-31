package com.example.satmattask.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.model.RechargeModel
import com.example.satmattask.utils.Utils

class ServiceRepository(val apiService: ApiInterface, val context: Context) {

    private val _serviceLiveData = MutableLiveData<Response<RechargeModel>>()

    val serviceLiveData: LiveData<Response<RechargeModel>>
        get() = _serviceLiveData

    suspend fun doRechargeRepository(
        memberId: String,
        apiPassword: String,
        apiPin: String,
        number: String
    ) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                val result = apiService.recharge(memberId, apiPassword, apiPin, number)
                if (result.body()?.ERROR == null) {
                    _serviceLiveData.postValue(Response.Success(result.body()))
                } else {
                    _serviceLiveData.postValue(Response.Error(result.body()))
                }
            } catch (e: Exception) {
                val rechargeModel = RechargeModel(2, "Unknown Error Occurred")
                _serviceLiveData.postValue(Response.Error(rechargeModel))
            }
        } else {
            val rechargeModel = RechargeModel(2, "No Network Available!!")
            _serviceLiveData.postValue(Response.Error(rechargeModel))
        }

    }
}