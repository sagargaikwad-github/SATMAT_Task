package com.example.satmattask.model.reports

data class RechargeHistory(
    val txnID: String,
    val numID: String,
    val operatorImage: String,
    val operatorName: String,
    val phoneNum: String,
    val openingBalance: String,
    val commissionBalance: String,
    val closingBalance: String,
    val balanceDate: String,
    val balanceStatue: String,
    val balancePrice: String
)
