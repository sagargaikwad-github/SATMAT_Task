package com.example.satmattask.api

import com.example.satmattask.model.RechargeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("/recharge_api/recharge")
    suspend fun recharge(
        @Query("member_id") memberId: String,
        @Query("api_password") api_password: String,
        @Query("api_pin") api_pin: String,
        @Query("number") number: String,
    ): Response<RechargeModel>
}