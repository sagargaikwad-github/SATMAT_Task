package com.example.satmattask.api

import com.example.satmattask.model.RechargeModel
import com.example.satmattask.model.getOperators.GetOperators
import com.example.satmattask.model.getOperators.GetOperatorsRequest
import com.example.satmattask.model.getRechargePlans.PlanResults
import com.example.satmattask.model.getRechargePlans.RechargePlans
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiInterface {
    @GET("/recharge_api/recharge")
    suspend fun recharge(
        @Query("member_id") memberId: String,
        @Query("api_password") api_password: String,
        @Query("api_pin") api_pin: String,
        @Query("number") number: String,
    ): Response<RechargeModel>


//    @FormUrlEncoded
//    @POST("/index.php/appapi/getOperators")
//    suspend fun getOperators(@Body getOperatorsRequest: GetOperatorsRequest) : Response<GetOperators>

    @FormUrlEncoded
    @POST("/index.php/appapi/getOperators")
    suspend fun getOperators(@FieldMap params: Map<String, String>) : Response<GetOperators>

    @FormUrlEncoded
    @POST("/index.php/appapi/roffer")
    suspend fun getRechargePlans(@FieldMap params: Map<String, String>) : Response<RechargePlans>
}