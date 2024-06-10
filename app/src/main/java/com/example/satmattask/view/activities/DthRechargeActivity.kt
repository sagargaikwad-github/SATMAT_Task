package com.example.satmattask.view.activities

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.adapter.OperatorBottomSheetAdapter
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.databinding.ActivityDthRechargeBinding
import com.example.satmattask.databinding.OperatorBottomsheetDialogBinding
import com.example.satmattask.model.getOperators.Result
import com.example.satmattask.repository.ResponseSealed
import com.example.satmattask.repository.ServiceRepository
import com.example.satmattask.utils.Utils
import com.example.satmattask.viewmodel.ServiceViewModel
import com.example.satmattask.viewmodel.ServiceViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.internal.UTC

class DthRechargeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDthRechargeBinding
    private lateinit var viewModel: ServiceViewModel
    private lateinit var repository: ServiceRepository
    private lateinit var apiInterface: ApiInterface
    private lateinit var bottomSheetOperatorsDialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDthRechargeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBar()
        statusBarColor()

        apiInterface = Utils.retrofitInstance.create(ApiInterface::class.java)
        repository = ServiceRepository(apiInterface, this)
        viewModel = ViewModelProvider(
            this,
            ServiceViewModelFactory(repository)
        ).get(ServiceViewModel::class.java)

        viewModel.getOperatorResponse.observe(this, Observer {
            Utils.hideDialog()
            when (it) {
                is ResponseSealed.Success -> {
                    if (it.data?.status == true) {
                        var operatorList = ArrayList<Result>()
                        operatorList.addAll(it.data.result)
                        openOperatorBottomSheet(operatorList)
                    } else {
                        Utils.showToast(this, it.data?.message!!)
                    }
                }

                is ResponseSealed.Error -> {
                    Utils.showToast(this, it.error?.message!!)
                }
            }
        })

        binding.apply {
            dthRechargeOperatorTV.setOnClickListener {
                Utils.showDialog(this@DthRechargeActivity)
                viewModel.getOperators("dth")
            }

            dthToolbar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun openOperatorBottomSheet(operatorList: ArrayList<Result>) {
        bottomSheetOperatorsDialog = BottomSheetDialog(this)
        val bottomView = LayoutInflater.from(this).inflate(
            com.example.satmattask.R.layout.operator_bottomsheet_dialog,
            null,
            false
        )
        val customViewBinding = OperatorBottomsheetDialogBinding.bind(bottomView)
        bottomSheetOperatorsDialog.setContentView(bottomView)

        val operatorBottomSheetAdapter = OperatorBottomSheetAdapter(::bottomSheetOperatorClicked)
        customViewBinding.dialogRV.apply {
            layoutManager = LinearLayoutManager(
                this@DthRechargeActivity,
                RecyclerView.VERTICAL, false
            )
            adapter = operatorBottomSheetAdapter
        }
        operatorBottomSheetAdapter.differ.submitList(operatorList)

        customViewBinding.dialogCancel.setOnClickListener {
            bottomSheetOperatorsDialog.dismiss()
        }
        bottomSheetOperatorsDialog.show()
    }


    fun bottomSheetOperatorClicked(result: Result) {
        if (bottomSheetOperatorsDialog != null) {
            bottomSheetOperatorsDialog.dismiss()
        }

        binding.dthRechargeOperatorTV.text = result.operatorname
        val operatorImageURL = Utils.BASE_URL + result.operator_image
        Glide.with(this).load(operatorImageURL)
            .into(binding.dthIV)
    }

    fun statusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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


}