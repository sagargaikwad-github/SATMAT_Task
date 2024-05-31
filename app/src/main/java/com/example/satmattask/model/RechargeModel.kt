package com.example.satmattask.model

import com.example.satmattask.repository.Response

data class RechargeModel(
    val ERROR: Int?=null,
    val MESSAGE: String?=null,
    val ip: String?=null,
    val request: Request?=null,
    val SUCCESS: String? = null
)