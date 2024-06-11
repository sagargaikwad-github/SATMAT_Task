package com.example.satmattask.Interface

interface OnZaakPayListener {
    fun PaymentSuccess(msg : String)
    fun PaymentFailed(msg : String)
}