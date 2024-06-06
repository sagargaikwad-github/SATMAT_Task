package com.example.satmattask.model.getRechargePlans

import com.google.gson.annotations.SerializedName

data class RechargePlans(
    @SerializedName("result")
    val result: PlanResults?,
    val status: Boolean?
)