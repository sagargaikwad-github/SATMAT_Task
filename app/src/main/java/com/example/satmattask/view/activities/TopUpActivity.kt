package com.example.satmattask.view.activities

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.satmattask.R
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.api.ExternalApiInterface
import com.example.satmattask.databinding.ActivityTopUpBinding
import com.example.satmattask.model.getOperators.Result
import com.example.satmattask.repository.ExternalApiRespository
import com.example.satmattask.repository.ResponseSealed
import com.example.satmattask.repository.ServiceRepository
import com.example.satmattask.utils.Utils
import com.example.satmattask.utils.Zaakpay
import com.example.satmattask.viewmodel.ExternalApiViewModel
import com.example.satmattask.viewmodel.ServiceViewModel
import com.mobikwik.mobikwikpglib.PaymentCheckout
import com.mobikwik.mobikwikpglib.lib.transactional.TransactionResponse
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class TopUpActivity : AppCompatActivity(), PaymentCheckout.ZaakPayPaymentListener {
    private lateinit var binding: ActivityTopUpBinding
    private lateinit var viewModel: ExternalApiViewModel
    private lateinit var repository: ExternalApiRespository
    var userSelectedMethod = 1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiInterface = Utils.externalRetrofitInstance.create(ExternalApiInterface::class.java)
        repository = ExternalApiRespository(apiInterface, this)
        viewModel = ExternalApiViewModel(repository)

        statusBarColor()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            topUpToolbar.setNavigationOnClickListener {
                finish()
            }

            initializeUPI()

            topUpZaakPay.setOnClickListener {
                if (binding.topUpEnterAmount.text.toString().length > 0) {
                    val enteredAmount = binding.topUpEnterAmount.text.toString().toInt()
                    if (enteredAmount >= 1) {
                        Zaakpay(this@TopUpActivity).startPayment(enteredAmount * 100)
                    } else {
                        Utils.showToast(this@TopUpActivity, "Minimum amount is 1")
                    }
                } else {
                    Utils.showToast(this@TopUpActivity, "Please enter amount")
                }

//                val topUpETNotEmpty = topUpEnterAmount.text.toString().length
//                if (topUpETNotEmpty > 1) {
//                    val enteredAmount = topUpEnterAmount.text.toString().toInt()
//                    if (enteredAmount < 1) {
//                        Utils.showToast(this@TopUpActivity, "Minimum amount is 1")
//                    } else {
//                        Zaakpay(this@TopUpActivity).startPayment(enteredAmount * 100)
//                    }
//                } else {
//                    Utils.showToast(this@TopUpActivity, "Please enter amount")
//                }
            }

            viewModel.getUpiIntentResponse.observe(this@TopUpActivity, Observer {
                Utils.hideDialog()
                when (it) {
                    is ResponseSealed.Success -> {
                        if (it.data?.doRedirect == "true") {
                            openUpiApp(it.data.bankPostData!!.androidIntentUrl)
                        } else {
                            Utils.showToast(
                                this@TopUpActivity,
                                it.data?.responseDescription.toString()
                            )
                        }
                    }

                    is ResponseSealed.Error -> {
                        Log.d("IntentResponse - Error", it.error.toString())
                    }
                }
            })
        }
    }

    private fun initializeUPI() {
        binding.apply {
            gPayIntent.setOnClickListener {
                if (topUpAmountAvailable()) {
                    val amount = binding.topUpEnterAmount.text.toString()
                    val data = """
            {"merchantIdentifier":"${Zaakpay.MERCHANT_IDENTIFIER}","showMobile":"true","mode":"0","returnUrl":"https://zaakstaging.zaakpay.com/api/automation/v1/payment/response","orderDetail":{"orderId":"900","amount":${amount.toInt() * 100},"currency":"INR","productDescription":"Test Automation","email":"sagargaikwad2017id@gmail.com","phone":"9075733851","extra1":"udf1","extra2":"udf2","extra3":"udf3","extra4":"udf4","extra5":"udf5","productDescription":"pd","product1Description":"pd1","product2Description":"pd2","product3Description":"pd3","product4Description":"pd4","firstName":"Test_Fir","lastName":"Test_Las"},"paymentInstrument":{"paymentMode":"UPIAPP","netbanking":{"bankid":""}},"billingAddress":{"city":"Gurgaon"},"shippingAddress":{"city":"Gurgaon"}}""".trimIndent()

                    val checkSumData = createChecksum(Zaakpay.SECRET_KEY, data)

                    userSelectedMethod = 2

                    Utils.showDialog(this@TopUpActivity)
                    viewModel.getUpiIntentResult(data, checkSumData)
                } else {
                    Utils.showToast(this@TopUpActivity, "Invalid Amount!!")
                }
            }
            phonePayIntent.setOnClickListener {
                if (topUpAmountAvailable()) {
                    val amount = binding.topUpEnterAmount.text.toString()
                    val data = """
            {"merchantIdentifier":"${Zaakpay.MERCHANT_IDENTIFIER}","showMobile":"true","mode":"0","returnUrl":"https://zaakstaging.zaakpay.com/api/automation/v1/payment/response","orderDetail":{"orderId":"900","amount":${amount.toInt() * 100},"currency":"INR","productDescription":"Test Automation","email":"sagargaikwad2017id@gmail.com","phone":"9075733851","extra1":"udf1","extra2":"udf2","extra3":"udf3","extra4":"udf4","extra5":"udf5","productDescription":"pd","product1Description":"pd1","product2Description":"pd2","product3Description":"pd3","product4Description":"pd4","firstName":"Test_Fir","lastName":"Test_Las"},"paymentInstrument":{"paymentMode":"UPIAPP","netbanking":{"bankid":""}},"billingAddress":{"city":"Gurgaon"},"shippingAddress":{"city":"Gurgaon"}}""".trimIndent()

//                    val paramsForPOChecksum = ("merchantIdentifier=" + Zaakpay.MERCHANT_IDENTIFIER
//                            + "&email=" + "sagargaikwad2017id@gmail.com")
                    val checkSumData = createChecksum(Zaakpay.SECRET_KEY, data)

                    userSelectedMethod = 3
                    Utils.showDialog(this@TopUpActivity)
                    viewModel.getUpiIntentResult(data, checkSumData)
                } else {
                    Utils.showToast(this@TopUpActivity, "Invalid Amount!!")
                }
            }
            paytmIntent.setOnClickListener {
                if (topUpAmountAvailable()) {
                    val amount = binding.topUpEnterAmount.text.toString()
                    val data = """
            {"merchantIdentifier":"${Zaakpay.MERCHANT_IDENTIFIER}","showMobile":"true","mode":"0","returnUrl":"https://zaakstaging.zaakpay.com/api/automation/v1/payment/response","orderDetail":{"orderId":"900","amount":${amount.toInt() * 100},"currency":"INR","productDescription":"Test Automation","email":"sagargaikwad2017id@gmail.com","phone":"9075733851","extra1":"udf1","extra2":"udf2","extra3":"udf3","extra4":"udf4","extra5":"udf5","productDescription":"pd","product1Description":"pd1","product2Description":"pd2","product3Description":"pd3","product4Description":"pd4","firstName":"Test_Fir","lastName":"Test_Las"},"paymentInstrument":{"paymentMode":"UPIAPP","netbanking":{"bankid":""}},"billingAddress":{"city":"Gurgaon"},"shippingAddress":{"city":"Gurgaon"}}""".trimIndent()

                    val paramsForPOChecksum = ("merchantIdentifier=" + Zaakpay.MERCHANT_IDENTIFIER
                            + "&email=" + "sagargaikwad2017id@gmail.com")
                    val checkSumData = createChecksum(Zaakpay.SECRET_KEY, data)

                    userSelectedMethod = 4

                    Utils.showDialog(this@TopUpActivity)
                    viewModel.getUpiIntentResult(data, checkSumData)
                } else {
                    Utils.showToast(this@TopUpActivity, "Invalid Amount!!")
                }
            }
            upiIntent.setOnClickListener {
                if (topUpAmountAvailable()) {
                    val amount = binding.topUpEnterAmount.text.toString()
                    val data = """
            {"merchantIdentifier":"${Zaakpay.MERCHANT_IDENTIFIER}","showMobile":"true","mode":"0","returnUrl":"https://zaakstaging.zaakpay.com/api/automation/v1/payment/response","orderDetail":{"orderId":"900","amount":${amount.toInt() * 100},"currency":"INR","productDescription":"Test Automation","email":"sagargaikwad2017id@gmail.com","phone":"9075733851","extra1":"udf1","extra2":"udf2","extra3":"udf3","extra4":"udf4","extra5":"udf5","productDescription":"pd","product1Description":"pd1","product2Description":"pd2","product3Description":"pd3","product4Description":"pd4","firstName":"Test_Fir","lastName":"Test_Las"},"paymentInstrument":{"paymentMode":"UPIAPP","netbanking":{"bankid":""}},"billingAddress":{"city":"Gurgaon"},"shippingAddress":{"city":"Gurgaon"}}""".trimIndent()

                    val paramsForPOChecksum = ("merchantIdentifier=" + Zaakpay.MERCHANT_IDENTIFIER
                            + "&email=" + "sagargaikwad2017id@gmail.com")
                    val checkSumData = createChecksum(Zaakpay.SECRET_KEY, data)

                    userSelectedMethod = 1

                    Utils.showDialog(this@TopUpActivity)
                    viewModel.getUpiIntentResult(data, checkSumData)
                } else {
                    Utils.showToast(this@TopUpActivity, "Invalid Amount!!")
                }
            }
        }
    }

    private fun openUpiApp(androidIntentUrl: String) {

        when (userSelectedMethod) {
            1 -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(androidIntentUrl))
                startActivityForResult(intent, 200)
            }

            2 -> {
                if (isAppInstalled("com.google.android.apps.nbu.paisa.user")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(androidIntentUrl))
                    intent.setPackage("com.google.android.apps.nbu.paisa.user")
                    startActivityForResult(intent, 200)
                }
            }

            3 -> {
                if (isAppInstalled("com.phonepe.app")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(androidIntentUrl))
                    intent.setPackage("com.phonepe.app")
                    startActivityForResult(intent, 200)
                }
            }

            4 -> {
                if (isAppInstalled("net.one97.paytm")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(androidIntentUrl))
                    intent.setPackage("net.one97.paytm")
                    startActivityForResult(intent, 200)
                }
            }
        }

    }

    private fun openIntent(paymentAPP: String, packageName: String) {
        val url =
            // "${paymentAPP}://pay?pa=mobikwik@appl&pn=Convenience+Test&tr=ZP5cea4576efa97&am=10&cu=INR&mc=0000"
            "${paymentAPP}://pay?pa=9075733851@ybl&pn=Convenience+Test&tr=ZP5cea4576efa97&am=10&cu=INR&mc=0000"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (packageName.isNotEmpty()) {
            intent.setPackage(packageName)
        }
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                //Utils.showToast(this, "Payment Success")
            } else if (resultCode == RESULT_CANCELED) {
                Utils.showToast(this, "Payment Cancelled")
            } else {
                Utils.showToast(this, "Payment Failed")
            }
        }
    }

    fun statusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimaryStatusBar)
        }
    }

    override fun onPaymentSuccess(p0: TransactionResponse?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentFailure(p0: String?, p1: String?) {
        Log.d("Payment Error 1 ", p0.toString())
        Log.d("Payment Error 2 ", p1.toString())
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
    }

    fun createChecksum(secretKey: String, allParamValueExceptChecksum: String): String {
        Log.d(Zaakpay.TAG, "In cryptoUtils createChecksum function")
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

    fun topUpAmountAvailable(): Boolean {
        if (binding.topUpEnterAmount.text.toString().length > 0) {
            val enteredAmount = binding.topUpEnterAmount.text.toString().toInt()
            if (enteredAmount >= 1) {
//                val enteredAmount = binding.topUpEnterAmount.text.toString().toInt()
//                if (enteredAmount < 1) {
//                    return false
//                } else {
//                    return true
//                }
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val pm: PackageManager = packageManager
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            Utils.showToast(this@TopUpActivity, "App Not Installed!!")
            return false
        }
    }

}

