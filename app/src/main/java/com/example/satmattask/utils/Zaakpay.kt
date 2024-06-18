package com.example.satmattask.utils

import android.content.Context
import android.util.Log
import com.mobikwik.mobikwikpglib.PaymentCheckout
import com.mobikwik.mobikwikpglib.PaymentCheckout.ZaakPayPaymentListener
import com.mobikwik.mobikwikpglib.lib.transactional.TransactionData
import com.mobikwik.mobikwikpglib.lib.transactional.TransactionDataBuilder
import com.mobikwik.mobikwikpglib.lib.transactional.TransactionResponse
import com.mobikwik.mobikwikpglib.utils.Enums
import com.mobikwik.mobikwikpglib.utils.Utils
import java.math.BigInteger
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class Zaakpay(val context: Context) {
    var enteredAmount_ = 0
    private var checksumTransact: String? = null
    private var checksumPO: String? = null


    companion object {
        const val TAG = "Demo"

        //Working but invalid key details
//        const val MERCHANT_IDENTIFIER = "b19e8f103bce406cbd3476431b6b7973"
//        const val SECRET_KEY = "0678056d96914a8583fb518caf42828a"

        //From documentation
//        const val MERCHANT_IDENTIFIER = "b19e8f103bce406cbd3476431b6b7973"
//        const val SECRET_KEY = "0678056d96914a8583fb518caf42828a"

        //Doc 2
//        const val MERCHANT_IDENTIFIER = "fb2016ffd3a64b2994a6289dc2b671a4"
//        const val SECRET_KEY = "0678056d96914a8583fb518caf42828a"

        //Ajinkya
        const val MERCHANT_IDENTIFIER = "5a06ad972155459eb8e37feec5dfef18"
        const val SECRET_KEY = "758e670ea3714084a80826a63015ab4c"

    }

    fun startPayment(enteredAmount: Int) {
        enteredAmount_ = enteredAmount
        setCheckSums()
        val transactionDataInstance: TransactionData = getTransactionDataInstance()
        val checkOut = PaymentCheckout(context)
        checkOut.startPayment(transactionDataInstance!!)
    }

    private fun getTransactionDataInstance(): TransactionData {
        val longNumber: Long = try {
            enteredAmount_.toLong()
        } catch (e: NumberFormatException) {
            -1L
        }
        val amount: BigInteger = BigInteger.valueOf(longNumber)

        return TransactionDataBuilder.TransactionDataBuilder()
            .withAmount(amount) // BigInteger
            .withChecksum(checksumTransact) // String
            .withChecksumPO(checksumPO) // String
            .withCurrency("INR") // String
            .withOrderId("1011") // String
            .withUserEmail("sagargaikwad2017id@gmail.com") // String
            .withReturnUrl("https://beta.zaakpay.com/testmerchant/sdkresponse") // Url on which the txn response will be posted (String)
            // .withMerchantIconUrl("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png") // Merchant Icon to be displayed in the SDK (String)
            .withMerchantIconUrl("https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fpay%20(1).png?alt=media&token=67af93dc-b745-4ad9-b0be-3720780cf047") // Merchant Icon to be displayed in the SDK (String)
            .withMerchantName("SATMAT") // String
            .withMerchantId(MERCHANT_IDENTIFIER) // String
            .withUserPhoneNumber("7821849722") //String
            .withEnvironment(Enums.Environment.PRODUCTION)
            .build()
    }

    private fun setCheckSums() {
        val ip = Utils.getLocalIpAddress()
        val date = Utils.getCurrentDate()

        val paramsForTransactChecksum =
            ((("'" + MERCHANT_IDENTIFIER + "''" +
                    "1011" + "''0''INR''" + enteredAmount_.toString() +
                    "''" + ip + "''" + date + "'")))

        val paramsForPOChecksum = ("merchantIdentifier=" + MERCHANT_IDENTIFIER
                + "&email=" + "sagargaikwad2017id@gmail.com")

        try {
            checksumTransact = createChecksum(SECRET_KEY, paramsForTransactChecksum)

            checksumPO = createChecksum(SECRET_KEY, paramsForPOChecksum)

        } catch (ex: java.lang.Exception) {
            Log.d(TAG, "checksum exception")
        }
    }

    fun createChecksum(secretKey: String, allParamValueExceptChecksum: String): String {
        Log.d(TAG, "In cryptoUtils createChecksum function")
        val dataToEncryptByte = allParamValueExceptChecksum.trim { it <= ' ' }.toByteArray()
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val checksumCalculatedByte = mac.doFinal(dataToEncryptByte)
        return toHex(checksumCalculatedByte)
    }

    private fun toHex(bytes: ByteArray): String {
        val buffer = StringBuilder(bytes.size * 2)
        var str: String
        for (b in bytes) {
            str = Integer.toHexString(b.toInt())
            val len = str.length
            if (len == 8) {
                buffer.append(str.substring(6))
            } else if (str.length == 2) {
                buffer.append(str)
            } else {
                buffer.append("0$str")
            }
        }
        return buffer.toString()
    }
}




