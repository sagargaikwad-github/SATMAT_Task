package com.example.satmattask.model.zaakPay

data class UpiIntentModel(
    val bankPostData: BankPostData?,
    val doRedirect: String,
    val orderDetail: OrderDetail?,
    val paymentInstrument: PaymentInstrument?,
    val paymentMode: String,
    val responseCode: String,
    val responseDescription: String
)

data class BankPostData(
    val androidIntentUrl: String,
    val gpayIntentIosUrl: String,
    val mbkIntentIosUrl: String,
    val paytmIntentIosUrl: String,
    val phonepeIntentIosUrl: String,
    val timeout: String,
    val token: String,
    val txnid: String
)

data class OrderDetail(
    val amount: String,
    val currency: String,
    val email: String,
    val extra1: String,
    val extra2: String,
    val extra3: String,
    val extra4: String,
    val extra5: String,
    val firstName: String,
    val lastName: String,
    val orderId: String,
    val phone: String,
    val product1Description: String,
    val product2Description: String,
    val product3Description: String,
    val product4Description: String,
    val productDescription: String
)

data class PaymentInstrument(
    val netbanking: Netbanking,
    val paymentMode: String
)

data class Netbanking(
    val bankid: String
)