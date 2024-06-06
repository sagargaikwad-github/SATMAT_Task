package com.example.satmattask.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.model.RechargeModel
import com.example.satmattask.model.getOperators.GetOperators
import com.example.satmattask.model.getOperators.GetOperatorsRequest
import com.example.satmattask.model.getOperators.Result
import com.example.satmattask.model.getRechargePlans.PlanResults
import com.example.satmattask.model.getRechargePlans.RechargePlans
import com.example.satmattask.utils.Utils

class ServiceRepository(val apiService: ApiInterface, val context: Context) {

    private val _serviceLiveData = MutableLiveData<ResponseSealed<RechargeModel>>()

    val serviceLiveData: LiveData<ResponseSealed<RechargeModel>>
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
                    _serviceLiveData.postValue(ResponseSealed.Success(result.body()))
                } else {
                    _serviceLiveData.postValue(ResponseSealed.Error(result.body()))
                }
            } catch (e: Exception) {
                val rechargeModel = RechargeModel(2, "Unknown Error Occurred")
                _serviceLiveData.postValue(ResponseSealed.Error(rechargeModel))
            }
        } else {
            val rechargeModel = RechargeModel(2, "No Network Available!!")
            _serviceLiveData.postValue(ResponseSealed.Error(rechargeModel))
        }

    }


    suspend fun getOperators(): ResponseSealed<GetOperators> {
        if (Utils.isNetworkAvailable(context)) {
            try {
                val params = mapOf(
                    "token" to Utils.authToken,
                    "operator_type" to "mobile"
                )
                //val getOperatorRequest = GetOperatorsRequest(Utils.authToken, "mobile")
                val request = apiService.getOperators(params)
                if (request.isSuccessful) {
                    return ResponseSealed.Success(request.body())
                } else {
                    return ResponseSealed.Error(request.body())
                }
            } catch (e: Exception) {
                Log.d("GetOperators Error", e.toString())
                var resultList: List<Result> = emptyList()
                val operatorModel = GetOperators("Unknown Error Occurred", resultList, false)
                return ResponseSealed.Error(operatorModel)
            }
        } else {
            val resultList: List<Result> = emptyList()
            val operatorModel = GetOperators("No Network Available!!", resultList, false)
            return ResponseSealed.Error(operatorModel)
        }
    }


    suspend fun getRechargePlans(operator: String, phone: String): ResponseSealed<RechargePlans> {
        if (Utils.isNetworkAvailable(context)) {
            try {
                val params = mapOf(
                    "token" to Utils.authToken,
                    "operator" to operator,
                    "mobile" to phone
                )
                val request = apiService.getRechargePlans(params)
                if (request.isSuccessful) {
                    return ResponseSealed.Success(request.body())
                } else {
                    return ResponseSealed.Error(request.body())
                }
            } catch (e: Exception) {
                val result = PlanResults("Invalid Operator", null, null, null, null)
                val response = RechargePlans(result, false)
                return ResponseSealed.Error(response)
            }
        } else {
            val result = PlanResults("No Network Available!!", null, null, null, null)
            val response = RechargePlans(result, false)
            return ResponseSealed.Error(response)
        }
    }
}