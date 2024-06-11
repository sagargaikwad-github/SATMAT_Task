package com.example.satmattask.view.activities

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
import com.example.satmattask.R
import com.example.satmattask.databinding.ActivityTopUpBinding
import com.example.satmattask.utils.Utils
import com.example.satmattask.utils.Zaakpay
import com.mobikwik.mobikwikpglib.PaymentCheckout
import com.mobikwik.mobikwikpglib.lib.transactional.TransactionResponse

class TopUpActivity : AppCompatActivity(), PaymentCheckout.ZaakPayPaymentListener {
    private lateinit var binding: ActivityTopUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            topUpZaakPay.setOnClickListener {
                val topUpETNotEmpty = topUpEnterAmount.text.toString().length
                if (topUpETNotEmpty > 1) {
                    val enteredAmount = topUpEnterAmount.text.toString().toInt()
                    if (enteredAmount < 1) {
                        Utils.showToast(this@TopUpActivity, "Minimum amount is 1")
                    } else {
                        Zaakpay(this@TopUpActivity).startPayment(enteredAmount * 100)
                    }
                } else {
                    Utils.showToast(this@TopUpActivity, "Please enter amount")
                }
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
}