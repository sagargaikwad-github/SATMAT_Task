package com.example.satmattask.api

import com.example.satmattask.model.zaakPay.UpiIntentModel
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ExternalApiInterface {
    @POST("/transactU?v=8")
    @FormUrlEncoded
    @Headers(
//        "cache-control : no-cache",
//        "content-type : application/x-www-form-urlencoded",
        "postman-token: c5b737cd-2f7a-0f85-c6cd-eb36700fd92f",
        "Cookie: JSESSIONID=4991D5810F65F0DAFC066FA155850005.11.36"
    )
    suspend fun getUpiIntentResponse(
        @FieldMap params: Map<String, String>
    ): Response<UpiIntentModel>
}