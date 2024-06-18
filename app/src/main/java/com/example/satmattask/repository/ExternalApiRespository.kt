package com.example.satmattask.repository

import android.content.Context
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.api.ExternalApiInterface
import com.example.satmattask.model.zaakPay.UpiIntentModel
import com.example.satmattask.utils.Utils

class ExternalApiRespository(val apiService: ExternalApiInterface, val context: Context) {
    suspend fun getUpiIntent(
        data: String,
        checkSUM: String
    ): ResponseSealed<UpiIntentModel> {
        if (Utils.isNetworkAvailable(context)) {
            try {
                val params = mapOf(
                    "data" to data,
                    "checksum" to checkSUM
                )

                val request = apiService.getUpiIntentResponse(params)
                if (request.isSuccessful) {
                    return ResponseSealed.Success(request.body())
                } else {
                    return ResponseSealed.Error(request.body())
                }
            } catch (e: Exception) {
                val result = UpiIntentModel(null, "false", null, null, "", "", e.message.toString())
                return ResponseSealed.Error(result)
            }
        } else {
            val result = UpiIntentModel(null, "false", null, null, "", "", "No Network Available!1")
            return ResponseSealed.Error(result)
        }
    }
}