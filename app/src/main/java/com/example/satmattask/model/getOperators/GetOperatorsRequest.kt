package com.example.satmattask.model.getOperators

import com.google.gson.annotations.SerializedName

data class GetOperatorsRequest(
    @SerializedName("token") val token: String,
    @SerializedName("operator_type") val operator_type: String
)
