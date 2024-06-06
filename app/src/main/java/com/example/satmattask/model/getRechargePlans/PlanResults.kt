package com.example.satmattask.model.getRechargePlans

import com.google.gson.annotations.SerializedName

data class PlanResults(
    val `operator`: String?,
    val records: List<Record>?,
    val status: Int?,
    val tel: String?,
    val time: Double?
)