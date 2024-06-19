package com.example.satmattask.view.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.databinding.ActivityMicroAtmactivityBinding
import com.example.satmattask.databinding.EnterAmountBottomshheetBinding
import com.example.satmattask.databinding.OperatorBottomsheetDialogBinding
import com.example.satmattask.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.service.finopayment.Hostnew
import okhttp3.internal.UTC
import java.util.Random

class MicroATMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMicroAtmactivityBinding

    var chars: CharArray = charArrayOf(
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        '0'
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMicroAtmactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            Glide.with(this@MicroATMActivity)
                .load("https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcity-hall.png?alt=media&token=fecfebf0-f626-4bf3-b1f6-b4d1b085bc25")
                .into(binding.withdrawnIV)
        }
        statusBar()
        setOnClickListeners()
    }

    private fun statusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //This related to status Bar Color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimaryStatusBar)
        }

    }

    private fun CheckSDK(txnType: String, amount: String) {
        val intent = Intent(
            applicationContext,
            Hostnew::class.java
        )
        intent.putExtra("partnerId", "PS005216")
        intent.putExtra(
            "apiKey",
            "UFMwMDUyMTYxYmE4ZGZiYmUyNzAyNmIwYjYzN2ZkZDI5YzBkMzRjOTE3MTQwMzkzMzY="
        )
        intent.putExtra("transactionType", txnType)
        intent.putExtra("amount", amount)
        intent.putExtra("merchantCode", "fp" + "9999991110")
        intent.putExtra("remarks", "Test Transaction")
        intent.putExtra("mobileNumber", "9999991110")
        intent.putExtra("referenceNumber", getRandomString(5, chars))
        intent.putExtra("latitude", "22.572646")
        intent.putExtra("longitude", "88.363895")
        intent.putExtra("subMerchantId", "fp" + "9999991110")
        intent.putExtra("deviceManufacturerId", "3")
        startActivityForResult(intent, 199)
    }

    private fun setOnClickListeners() {
        binding.balanceCheckBTN.setOnClickListener {
            CheckSDK("ATMBE", "00")
        }

        binding.withdrawnSection.setOnClickListener {
//            if (binding.enterAmountET.text.length < 1) {
//                Utils.showToast(this, "Please enter amount!!")
//                return@setOnClickListener
//            }
            //   CheckSDK("ATMCW", binding.enterAmountET.text.toString())

            openAmountBottomSheet()
        }
        binding.microATMToolbar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun openAmountBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomView = LayoutInflater.from(this).inflate(
            com.example.satmattask.R.layout.enter_amount_bottomshheet,
            null,
            false
        )
        val customViewBinding = EnterAmountBottomshheetBinding.bind(bottomView)
        bottomSheetDialog.setContentView(bottomView)

        customViewBinding.withdrawnBTN.setOnClickListener {
            if (customViewBinding.enterAmountET.text!!.length < 1) {
                Utils.showToast(this, "Please enter amount!!")
                return@setOnClickListener
            }
            CheckSDK("ATMCW", customViewBinding.enterAmountET.text.toString())
            bottomSheetDialog.dismiss()
        }

        customViewBinding.dialogCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 199) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    var status = data.getBooleanExtra("status", false)
                    var response = data.getIntExtra("response", 0)
                    var message = data.getStringExtra("message")
                    var dataResponse = data.getStringExtra("data:response")
                    var dataTransAmount = data.getStringExtra("data:transAmount")
                    var dataBalAmount = data.getStringExtra("data:balAmount")
                    var dataBalRrn = data.getStringExtra("data:bankRrn")
                    var dataTxnId = data.getStringExtra("data:txnid")
                    var dataTransType = data.getStringExtra("data:transType")
                    var dataType = data.getStringExtra("data:type")
                    var dataCardNumber = data.getStringExtra("data:cardNumber")
                    var dataCardType = data.getStringExtra("data:cardType")
                    var dataTerminalId = data.getStringExtra("data:terminalId")
                    var dataBankName = data.getStringExtra("data:bankName")

                    Toast.makeText(
                        applicationContext,
                        "$message Balance:$dataBalAmount",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun getRandomString(length: Int, characterSet: CharArray): String {
        val sb = StringBuilder()

        for (loop in 0 until length) {
            val index = Random().nextInt(characterSet.size)
            sb.append(characterSet[index])
        }

        val nonce = sb.toString()
        return nonce
    }
}