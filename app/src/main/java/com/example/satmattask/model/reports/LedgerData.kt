package com.example.satmattask.model.reports

data class LedgerData(
    val txnID: String,
    val ledgerDate: String,
    val transactionType: String,
    val openingBalance: String,
    val creditedBalance: String,
    val debitedBalance: String,
    val closingBalance: String,
)
